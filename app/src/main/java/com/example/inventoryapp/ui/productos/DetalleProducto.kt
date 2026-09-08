package com.example.inventoryapp.ui.productos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.inventoryapp.R
import com.inventoryapp.data.database.InventoryDatabase
import com.example.inventoryapp.data.remote.dto.ProductoDto
import com.example.inventoryapp.data.remote.retrofit.RetrofitClient
import com.example.inventoryapp.data.repository.ProductoApiRepository
import com.inventoryapp.data.repository.ProductoRepository
import com.example.inventoryapp.data.utils.NetworkUtils
import com.inventoryapp.data.entity.Producto
import kotlinx.coroutines.launch

class DetalleProducto : AppCompatActivity() {

    private var imagenSeleccionada: Uri? = null

    private val seleccionarImagen =
        registerForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri ->

            if (uri != null) {

                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                imagenSeleccionada = uri
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_producto)

        val imgSubirImagen =
            findViewById<ImageView>(R.id.imgSubir)

        imgSubirImagen.setOnClickListener {
            seleccionarImagen.launch(arrayOf("image/*"))
        }

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v, insets ->

            val systemBars =
                insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                )

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        val btnguardar =
            findViewById<Button>(R.id.btnGuardarProducto)

        val edtNombreProducto =
            findViewById<EditText>(R.id.edtNombreProducto)

        val edtCodigo =
            findViewById<EditText>(R.id.edtCodigo)

        val edtCategoria =
            findViewById<AutoCompleteTextView>(R.id.edtCategoria)

        val edtPrecio =
            findViewById<EditText>(R.id.edtPrecio)

        val edtCantidad =
            findViewById<EditText>(R.id.edtCantidad)

        val categorias = listOf(
            "Computadoras",
            "Accesorios",
            "Celulares",
            "Impresoras",
            "Componentes",
            "Otros"
        )

        val adapterCategorias = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            categorias
        )

        edtCategoria.setAdapter(adapterCategorias)
        edtCategoria.threshold = 0

        edtCategoria.setOnClickListener {
            edtCategoria.showDropDown()
        }

        // Base de datos local Room
        val database =
            InventoryDatabase.getDatabase(applicationContext)

        val dao = database.productoDao()

        val repository =
            ProductoRepository(dao)

        // API de Azure
        val apiRepository =
            ProductoApiRepository(
                RetrofitClient.create(this)
            )

        btnguardar.setOnClickListener {

            val nombre =
                edtNombreProducto.text.toString().trim()

            val codigo =
                edtCodigo.text.toString().trim()

            val categoria =
                edtCategoria.text.toString().trim()

            val precioTexto =
                edtPrecio.text.toString().trim()

            val cantidadTexto =
                edtCantidad.text.toString().trim()

            // Validaciones básicas
            if (nombre.isEmpty() ||
                codigo.isEmpty() ||
                categoria.isEmpty() ||
                precioTexto.isEmpty() ||
                cantidadTexto.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Completa todos los campos.",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val precio =
                precioTexto.toDoubleOrNull()

            val cantidad =
                cantidadTexto.toIntOrNull()

            if (precio == null || cantidad == null) {

                Toast.makeText(
                    this,
                    "Precio o cantidad no válidos.",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val hayInternet =
                NetworkUtils.hayInternet(this)


             // El producto siempre se crea primero en Room con sincronizado = false
             // Solo después de recibir confirmación de Azure lo marcamos como sincronizado.

            val producto = Producto(
                nombre = nombre,
                precio = precio,
                cantidad = cantidad,
                categoria = categoria,
                codigo = codigo,
                imagen = imagenSeleccionada?.toString() ?: "",
                sincronizado = false
            )

            lifecycleScope.launch {

                try {

                    // Guardar primero en Room
                    repository.insertar(producto)

                    if (hayInternet) {

                        // Preparamos el DTO utilizando syncId como identificador
                        val productoDto = ProductoDto(
                            id = null,
                            syncId = producto.syncId,
                            nombre = producto.nombre,
                            precio = producto.precio,
                            cantidad = producto.cantidad,
                            categoria = producto.categoria,
                            codigo = producto.codigo,
                            imagen = producto.imagen
                        )

                        // Enviar a Azure
                        val respuesta =
                            apiRepository.guardarProducto(
                                productoDto
                            )

                        // Verificar que Azure devolvió el ID
                        val serverId = respuesta.id

                        if (serverId == null) {
                            throw Exception("Azure no devolvió el ID del producto.")
                        }

                        // Guardar el ID de Azure y marcar como sincronizado
                        repository.marcarComoSincronizado(
                            producto.syncId,
                            serverId
                        )

                        Toast.makeText(
                            this@DetalleProducto,
                            "Producto guardado y sincronizado correctamente.",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {

                        Toast.makeText(
                            this@DetalleProducto,
                            "Sin conexión: el producto se guardó localmente y quedará pendiente de sincronización.",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    // Limpiar campos
                    edtNombreProducto.text.clear()
                    edtCodigo.text.clear()
                    edtCategoria.setText("", false)
                    edtPrecio.text.clear()
                    edtCantidad.text.clear()

                    // Regresar al módulo de productos
                    val intent =
                        Intent(
                            this@DetalleProducto,
                            ModuloProducto::class.java
                        )

                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TOP

                    startActivity(intent)
                    finish()

                } catch (e: Exception) {


                     // Si Azure falla, el producto YA está en Room con sincronizado = false, por lo tanto NO se pierde.
                     //Posteriormente WorkManager podrá enviarlo.

                    Toast.makeText(
                        this@DetalleProducto,
                        "Producto guardado localmente. Pendiente de sincronización.",
                        Toast.LENGTH_LONG
                    ).show()

                    val intent =
                        Intent(
                            this@DetalleProducto,
                            ModuloProducto::class.java
                        )

                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TOP

                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}
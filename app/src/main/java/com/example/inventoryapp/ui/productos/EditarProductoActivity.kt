package com.example.inventoryapp.ui.productos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.inventoryapp.data.remote.dto.ProductoDto
import com.example.inventoryapp.data.remote.retrofit.RetrofitClient
import com.example.inventoryapp.databinding.ActivityEditarProductoBinding
import com.inventoryapp.data.database.InventoryDatabase
import com.inventoryapp.data.entity.Producto
import com.inventoryapp.data.repository.ProductoRepository
import com.inventoryapp.viewmodel.ProductoViewModel
import kotlinx.coroutines.launch

class EditarProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarProductoBinding
    private lateinit var viewModel: ProductoViewModel

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

        binding = ActivityEditarProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->

            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        // Inicializar Room
        val database =
            InventoryDatabase.getDatabase(applicationContext)

        val dao = database.productoDao()

        val repository =
            ProductoRepository(dao)

        val factory =
            object : ViewModelProvider.Factory {

                override fun <T : ViewModel> create(
                    modelClass: Class<T>
                ): T {

                    if (
                        modelClass.isAssignableFrom(
                            ProductoViewModel::class.java
                        )
                    ) {
                        return ProductoViewModel(repository) as T
                    }

                    throw IllegalArgumentException(
                        "Unknown ViewModel class"
                    )
                }
            }

        viewModel = ViewModelProvider(this, factory) [ProductoViewModel::class.java]

        // Recuperar información del producto
        val id =
            intent.getIntExtra("id", 0)

        val syncId =
            intent.getStringExtra("syncId") ?: ""

        val nombre =
            intent.getStringExtra("nombre") ?: ""

        val serverId =
            intent.getIntExtra("serverId", 0)
        android.util.Log.d(
            "EDITAR_DEBUG",
            "id=$id | syncId='$syncId' | serverId=$serverId | nombre=$nombre")

        val categoria =
            intent.getStringExtra("categoria") ?: ""

        val codigo =
            intent.getStringExtra("codigo") ?: ""

        val precio =
            intent.getDoubleExtra("precio", 0.0)

        val cantidad =
            intent.getIntExtra("cantidad", 0)

        val imagenAnterior =
            intent.getStringExtra("imagen") ?: ""

        // Seleccionar imagen
        binding.imgSubir2.setOnClickListener {

            seleccionarImagen.launch(
                arrayOf("image/*")
            )
        }

        // Mostrar datos actuales
        binding.edtNombreProducto.setText(nombre)
        binding.edtCategoria.setText(categoria)
        binding.edtPrecio.setText(precio.toString())
        binding.edtCantidad.setText(cantidad.toString())

        // Actualizar
        binding.btnActualizar.setOnClickListener {

            val nuevoNombre =
                binding.edtNombreProducto.text
                    .toString()
                    .trim()

            val nuevaCategoria =
                binding.edtCategoria.text
                    .toString()
                    .trim()

            val nuevoPrecioStr =
                binding.edtPrecio.text
                    .toString()
                    .trim()

            val nuevaCantidadStr =
                binding.edtCantidad.text
                    .toString()
                    .trim()

            // Validación
            if (
                nuevoNombre.isEmpty() ||
                nuevaCategoria.isEmpty() ||
                nuevoPrecioStr.isEmpty() ||
                nuevaCantidadStr.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Por favor llena todos los campos",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val nuevoPrecio =
                nuevoPrecioStr.toDoubleOrNull()

            val nuevaCantidad =
                nuevaCantidadStr.toIntOrNull()

            if (
                nuevoPrecio == null ||
                nuevaCantidad == null
            ) {

                Toast.makeText(
                    this,
                    "Precio o cantidad no válidos",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // Conservar imagen si no se seleccionó otra
            val nuevaImagen =
                imagenSeleccionada?.toString()
                    ?: imagenAnterior

            // Crear producto actualizado para Room
            val productoActualizado =
                Producto(
                    id = id,
                    syncId = syncId,
                    serverId =
                        if (serverId == 0) null
                        else serverId,
                    nombre = nuevoNombre,
                    precio = nuevoPrecio,
                    cantidad = nuevaCantidad,
                    categoria = nuevaCategoria,
                    codigo = codigo,
                    imagen = nuevaImagen,
                    sincronizado = false
                )

            // Primero actualizamos localmente
            viewModel.actualizarProducto(
                productoActualizado
            )

            // Si el producto ya existe en Azure,
            // actualizamos mediante PUT.
            if (serverId != 0) {

                lifecycleScope.launch {

                    try {

                        val api =
                            RetrofitClient.create(
                                this@EditarProductoActivity
                            )

                        val productoDto =
                            ProductoDto(
                                id = serverId,
                                syncId = syncId,
                                nombre = nuevoNombre,
                                precio = nuevoPrecio,
                                cantidad = nuevaCantidad,
                                categoria = nuevaCategoria,
                                codigo = codigo,
                                imagen = nuevaImagen
                            )

                        // Esperamos realmente la respuesta de Azure
                        api.actualizarProducto(
                            serverId,
                            productoDto
                        )

                        // Azure confirmó la actualización.
                        // Ahora marcamos Room como sincronizado.
                        viewModel.marcarComoSincronizado(
                            syncId,
                            serverId
                        )

                        Toast.makeText(
                            this@EditarProductoActivity,
                            "Producto actualizado correctamente",
                            Toast.LENGTH_SHORT
                        ).show()

                        regresarAProductos()

                    } catch (e: Exception) {

                        // Si falla Azure, el producto queda
                        // como pendiente de sincronización.
                        Toast.makeText(
                            this@EditarProductoActivity,
                            "Actualizado localmente. Se sincronizará cuando haya conexión.",
                            Toast.LENGTH_LONG
                        ).show()

                        regresarAProductos()
                    }
                }

            } else {

                // Producto que todavía no tiene ID de Azure.
                // Se queda pendiente para sincronización.
                Toast.makeText(
                    this,
                    "Producto actualizado localmente. Se sincronizará después.",
                    Toast.LENGTH_SHORT
                ).show()

                regresarAProductos()
            }
        }
    }

    private fun regresarAProductos() {

        val intent =
            Intent(
                this,
                ModuloProducto::class.java
            )

        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP

        startActivity(intent)

        finish()
    }
}
package com.example.inventoryapp.ui.productos

// Importaciones necesarias
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.inventoryapp.R
import com.inventoryapp.data.database.InventoryDatabase
import com.inventoryapp.data.entity.Producto
import com.inventoryapp.data.repository.ProductoRepository
import com.inventoryapp.viewmodel.ProductoViewModel
import com.example.inventoryapp.data.remote.retrofit.RetrofitClient
import com.example.inventoryapp.data.repository.ProductoApiRepository
import com.example.inventoryapp.viewmodel.ProductoApiViewModel
import com.example.inventoryapp.data.utils.NetworkUtils // NUEVO: para verificar conexión
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class DetalleProductoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Activa el diseño de pantalla completa.
        enableEdgeToEdge()

        // Carga el diseño XML de la pantalla Detalle del producto.
        setContentView(R.layout.detalle_producto)

        val imgProducto =
            findViewById<ImageView>(R.id.imgProducto)

        val imagen = intent.getStringExtra("imagen")

        if (!imagen.isNullOrEmpty()) {
            try {
                imgProducto.setImageURI(Uri.parse(imagen))
            } catch (e: SecurityException) {
                imgProducto.setImageURI(null)
            } catch (e: Exception) {
                imgProducto.setImageURI(null)
            }
        }

        // Obtenemos los datos enviados desde la pantalla anterior
        val id = intent.getIntExtra("id", -1)
        val syncId = intent.getStringExtra("syncId") ?: ""
        val serverId = intent.getIntExtra("serverId", 0)
        val nombre = intent.getStringExtra("nombre") ?: ""
        val codigo = intent.getStringExtra("codigo") ?: ""
        val categoria = intent.getStringExtra("categoria") ?: ""
        val precio = intent.getDoubleExtra("precio", 0.0)
        val cantidad = intent.getIntExtra("cantidad", 0)

        // Vinculamos los elementos del diseño (XML) con el código
        val txtNombreProducto = findViewById<TextView>(R.id.tvNombreProducto)
        val txtCodigo = findViewById<TextView>(R.id.tvCodigoProducto)
        val txtCategoria = findViewById<TextView>(R.id.tvCategoriaProducto)
        val txtPrecio = findViewById<TextView>(R.id.tvPrecioProducto)
        val txtcantidad = findViewById<TextView>(R.id.tvCantidad)
        val btnEliminar = findViewById<Button>(R.id.btnEliminar)

        //Mostramos los datos en pantalla
        txtNombreProducto.text = nombre
        txtCodigo.text = codigo
        txtCategoria.text = categoria
        txtPrecio.text = "C$${String.format("%.2f", precio)}"
        txtcantidad.text = "$cantidad"

        val btnEditar = findViewById<Button>(R.id.btnEditar)
        btnEditar.setOnClickListener {
            val intent = Intent(this, EditarProductoActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("syncId", syncId)
            intent.putExtra("serverId", serverId)
            intent.putExtra("nombre", nombre)
            intent.putExtra("codigo", codigo)
            intent.putExtra("categoria", categoria)
            intent.putExtra("precio", precio)
            intent.putExtra("cantidad", cantidad)
            intent.putExtra("imagen", imagen)

            startActivity(intent)
        }

        // Configuramos el ViewModel para acceder a la base de datos
        val database = InventoryDatabase.getDatabase(applicationContext)
        val dao = database.productoDao()
        val repository = ProductoRepository(dao)
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ProductoViewModel::class.java)) {
                    return ProductoViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
        val viewModel = ViewModelProvider(this, factory)[ProductoViewModel::class.java]

        // Programamos el evento clic del botón Eliminar
        btnEliminar.setOnClickListener {

            AlertDialog.Builder(this)
                .setTitle("Eliminar producto")
                .setMessage("¿Desea eliminar definitivamente este producto?")
                .setPositiveButton("Sí") { _, _ ->

                    lifecycleScope.launch {

                        val hayInternet =
                            NetworkUtils.hayInternet(this@DetalleProductoActivity)

                        if (hayInternet) {

                            try {

                                // API de Azure
                                val apiRepository =
                                    ProductoApiRepository(
                                        RetrofitClient.create(
                                            this@DetalleProductoActivity
                                        )
                                    )

                                // Primero eliminamos de Azure
                                if (serverId != 0) {

                                    apiRepository.eliminarProducto(serverId)

                                }

                                // Si Azure confirmó el borrado,
                                // eliminamos también de Room.
                                viewModel.eliminarPorId(id)

                                Toast.makeText(
                                    this@DetalleProductoActivity,
                                    "Producto eliminado correctamente",
                                    Toast.LENGTH_SHORT
                                ).show()

                                finish()

                            } catch (e: Exception) {

                                Toast.makeText(
                                    this@DetalleProductoActivity,
                                    "No se pudo eliminar el producto de Azure.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        } else {

                            // Sin Internet:
                            // lo marcamos como pendiente de eliminación.
                            val productoPendienteEliminar =
                                Producto(
                                    id = id,
                                    serverId =
                                        if (serverId == 0) null
                                        else serverId,
                                    nombre = nombre,
                                    precio = precio,
                                    cantidad = cantidad,
                                    categoria = categoria,
                                    codigo = codigo,
                                    imagen = imagen ?: "",
                                    pendienteEliminar = true
                                )

                            viewModel.actualizarProducto(
                                productoPendienteEliminar
                            )

                            Toast.makeText(
                                this@DetalleProductoActivity,
                                "Sin conexión: se eliminará cuando vuelva Internet.",
                                Toast.LENGTH_LONG
                            ).show()

                            finish()
                        }
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }

        // Permite adaptar la pantalla a las barras del sistema.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->

            // Obtiene el tamaño de las barras del sistema.
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Aplica el espacio necesario para que los elementos no queden debajo de las barras del sistema.
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            // Devuelve los Insets para continuar el procesamiento.
            insets
        }
    }
}
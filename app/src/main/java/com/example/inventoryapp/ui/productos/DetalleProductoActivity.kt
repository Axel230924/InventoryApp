package com.example.inventoryapp.ui.productos

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.inventoryapp.R
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inventoryapp.data.database.InventoryDatabase
import com.inventoryapp.data.repository.ProductoRepository
import com.inventoryapp.viewmodel.ProductoViewModel
import com.inventoryapp.data.entity.Producto

class DetalleProductoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Activa el diseño de pantalla completa.
        enableEdgeToEdge()

        // Carga el diseño XML de la pantalla Detalle del producto.
        setContentView(R.layout.detalle_producto)

        // Paso 1: Obtenemos los datos enviados desde la pantalla anterior
        val id = intent.getIntExtra("id", -1)
        val nombre = intent.getStringExtra("nombre") ?: ""
        val codigo = intent.getStringExtra("codigo") ?: ""
        val categoria = intent.getStringExtra("categoria") ?: ""
        val precio = intent.getDoubleExtra("precio", 0.0)
        val cantidad = intent.getIntExtra("cantidad", 0)

        // Paso 2: Vinculamos los elementos del diseño (XML) con el código
        val txtNombreProducto = findViewById<TextView>(R.id.tvNombreProducto)
        val txtCodigo = findViewById<TextView>(R.id.tvCodigoProducto)
        val txtCategoria = findViewById<TextView>(R.id.tvCategoriaProducto)
        val txtPrecio = findViewById<TextView>(R.id.tvPrecioProducto)
        val txtcantidad = findViewById<TextView>(R.id.tvCantidad)
        val btnEliminar = findViewById<Button>(R.id.btnEliminar)

        // Paso 3: Mostramos los datos en pantalla
        txtNombreProducto.text = nombre
        txtCodigo.text = codigo
        txtCategoria.text = categoria
        txtPrecio.text = "C$${String.format("%.2f", precio)}"
        txtcantidad.text = "$cantidad"

        // Paso 4: Configuramos el ViewModel para acceder a la base de datos
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

        // Paso 5: Programamos el evento click del botón Eliminar
        btnEliminar.setOnClickListener {
            // Paso 5.1: Creamos un mensaje de alerta para confirmar el borrado
            AlertDialog.Builder(this)
                .setTitle("Eliminar producto")
                .setMessage("¿Desea eliminar definitivamente este producto?")
                .setPositiveButton("Sí") { _, _ ->
                    // Paso 5.2: Si confirma, creamos el objeto producto con su ID
                    val productoAEliminar = Producto(
                        id = id,
                        nombre = nombre,
                        precio = precio,
                        cantidad = cantidad,
                        categoria = categoria,
                        codigo = codigo
                    )
                    // Paso 5.3: Llamamos al ViewModel para borrarlo de la base de datos
                    viewModel.eliminarProducto(productoAEliminar)

                    // Mostramos un mensaje de éxito
                    Toast.makeText(this, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show()

                    // Paso 5.4: Cerramos esta pantalla para volver a la lista
                    finish()
                }
                .setNegativeButton("No", null) // Si presiona No, no hace nada
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
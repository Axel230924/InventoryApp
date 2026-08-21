package com.example.inventoryapp.ui.productos

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.inventoryapp.R
import com.example.inventoryapp.databinding.ActivityEditarProductoBinding
import com.inventoryapp.data.database.InventoryDatabase
import com.inventoryapp.data.entity.Producto
import com.inventoryapp.data.repository.ProductoRepository
import com.inventoryapp.viewmodel.ProductoViewModel

class EditarProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarProductoBinding
    private lateinit var viewModel: ProductoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        binding = ActivityEditarProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar el ViewModel
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
        viewModel = ViewModelProvider(this, factory)[ProductoViewModel::class.java]

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Recuperar los datos enviados desde la pantalla anterior
        val id = intent.getIntExtra("id", 0)
        val nombre = intent.getStringExtra("nombre")
        val categoria = intent.getStringExtra("categoria")
        val codigo = intent.getStringExtra("codigo")
        val precio = intent.getDoubleExtra("precio", 0.0)
        val cantidad = intent.getIntExtra("cantidad", 0)

        // Asignar los valores a los campos de la pantalla (UI)
        binding.edtNombreProducto.setText(nombre)
        binding.edtCategoria.setText(categoria)
        binding.edtPrecio.setText(precio.toString())
        binding.edtCantidad.setText(cantidad.toString())


        // --- AQUÍ DEBE IR EL LISTENER ---
        binding.btnActualizar.setOnClickListener {

            val nuevoNombre = binding.edtNombreProducto.text.toString()
            val nuevaCategoria = binding.edtCategoria.text.toString()
            val nuevoPrecioStr = binding.edtPrecio.text.toString()
            val nuevaCantidadStr = binding.edtCantidad.text.toString()

            // Validación básica de campos vacíos o incorrectos
            if (nuevoNombre.isEmpty() || nuevoPrecioStr.isEmpty() || nuevaCantidadStr.isEmpty()) {
                Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nuevoPrecio = nuevoPrecioStr.toDoubleOrNull() ?: 0.0
            val nuevaCantidad = nuevaCantidadStr.toIntOrNull() ?: 0

            val productoActualizado = Producto(
                id = id,
                nombre = nuevoNombre,
                precio = nuevoPrecio,
                cantidad = nuevaCantidad,
                categoria = nuevaCategoria,
                codigo = codigo ?: "", // Usamos el código original recuperado del intent
                imagen = ""
            )

            viewModel.actualizarProducto(productoActualizado)

            Toast.makeText(
                this,
                "Producto actualizado con éxito",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
        
    }
}
package com.example.inventoryapp.ui.productos

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.inventoryapp.R
import androidx.lifecycle.ViewModelProvider
import com.inventoryapp.data.entity.Producto
import com.inventoryapp.viewmodel.ProductoViewModel
import com.inventoryapp.data.database.InventoryDatabase
import com.inventoryapp.data.repository.ProductoRepository
import android.net.Uri
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts

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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnguardar = findViewById<Button>(R.id.btnGuardarProducto)
        val edtNombreProducto = findViewById<EditText>(R.id.edtNombreProducto)
        val edtCodigo = findViewById<EditText>(R.id.edtCodigo)
        val edtCategoria = findViewById<AutoCompleteTextView>(R.id.edtCategoria)
        val edtPrecio = findViewById<EditText>(R.id.edtPrecio)
        val edtCantidad = findViewById<EditText>(R.id.edtCantidad)

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
        val database = InventoryDatabase.getDatabase(applicationContext)

        val dao = database.productoDao()

        val repository = ProductoRepository(dao)

        val factory = object : ViewModelProvider.Factory {

            override fun <T : androidx.lifecycle.ViewModel> create(
                modelClass: Class<T>
            ): T {

                if (modelClass.isAssignableFrom(ProductoViewModel::class.java)) {
                    return ProductoViewModel(repository) as T
                }

                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        val viewModel = ViewModelProvider(
            this,
            factory
        )[ProductoViewModel::class.java]

        btnguardar.setOnClickListener {
            val nombre = edtNombreProducto.text.toString()
            val codigo = edtCodigo.text.toString()
            val categoria = edtCategoria.text.toString()
            val precio = edtPrecio.text.toString().toDouble()
            val cantidad = edtCantidad.text.toString().toInt()

            val producto = Producto(
                nombre = nombre,
                categoria = categoria,
                codigo = codigo,
                precio = precio,
                cantidad = cantidad,
                imagen = imagenSeleccionada?.toString() ?: ""
            )

            viewModel.guardarProducto(
                producto
            )

            Toast.makeText(
                this,
                "Producto guardado",
                Toast.LENGTH_LONG
            ).show()

            edtNombreProducto.text.clear()
            edtCodigo.text.clear()
            edtCategoria.setText("", false)
            edtPrecio.text.clear()
            edtCantidad.text.clear()
        }
    }
}
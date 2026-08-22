package com.example.inventoryapp.ui.productos

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inventoryapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton // Boton Flotante
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inventoryapp.data.database.InventoryDatabase  // Importamos Inventory
import com.inventoryapp.data.repository.ProductoRepository // El repository
import com.inventoryapp.viewmodel.ProductoViewModel // El ViewModel

class ModuloProducto : AppCompatActivity() {
    private var drawerLayout: DrawerLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_producto)

        // Nos permite recrear y traer el flujo de la base de datos para poder utilizarlo aquí
        // Database --> DAO --> Repository --> ViewModel
        val database = InventoryDatabase.getDatabase(applicationContext)
        val dao = database.productoDao()
        val repository = ProductoRepository(dao)

        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Accedemos al recyclerView
        val recyclerProductos = findViewById<RecyclerView>(R.id.rvProductos)
        recyclerProductos.layoutManager = LinearLayoutManager(this)

        // Creamos la instancia al adapter y nos permitirá utilizarlo en el recycler.
        //val adapter = ProductoAdapter()


        val adapter = ProductoAdapter{producto -> val intent = Intent(this,DetalleProductoActivity::class.java)
            intent.putExtra("id", producto.id)
            intent.putExtra("nombre", producto.nombre)
            intent.putExtra("categoria", producto.categoria)
            intent.putExtra("codigo", producto.codigo)
            intent.putExtra("precio", producto.precio)
            intent.putExtra("cantidad", producto.cantidad)
            startActivity(intent)
        }
        recyclerProductos.adapter = adapter

        // Permite preguntar si desde el ViewModel nos envia Productos
        viewModel.productos.observe(this) { productos ->
            adapter.submitList(productos)  // Si hay, se los pasa al adapter
        }

        // Realizamos un evento, al hacer click en el botón agregar se nos abrirá la pantalla detalleProducto
        val newProducto= findViewById<FloatingActionButton>(R.id.btnAgregarProducto)
        newProducto.setOnClickListener {
            val intent= Intent(this, DetalleProducto::class.java)
            startActivity(intent)
        }

        // Permite abrir el menú si presionamos el boton menú
        val btnMenuP = findViewById<ImageView>(R.id.imgBtnMenu)
        btnMenuP.setOnClickListener {
            drawerLayout?.openDrawer(GravityCompat.START)
        }
    }

}
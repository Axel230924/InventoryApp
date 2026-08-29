package com.example.inventoryapp.ui.productos

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inventoryapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inventoryapp.data.database.InventoryDatabase
import com.inventoryapp.data.repository.ProductoRepository
import com.inventoryapp.viewmodel.ProductoViewModel
import com.example.inventoryapp.ui.productos.DetalleProductoActivity
import com.example.inventoryapp.data.remote.retrofit.RetrofitClient
import com.example.inventoryapp.data.remote.dto.toEntity
import com.example.inventoryapp.data.repository.ProductoApiRepository
import com.example.inventoryapp.viewmodel.ProductoApiViewModel
import com.example.inventoryapp.data.utils.NetworkUtils
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ModuloProducto : AppCompatActivity() {

    // Las guardamos como propiedades de la clase para poder
    // reutilizarlas en onResume() sin volver a crearlas
    private lateinit var apiViewModel: ProductoApiViewModel
    private lateinit var viewModel: ProductoViewModel
    private lateinit var adapter: ProductoAdapter

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
        viewModel = ViewModelProvider(
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
        adapter = ProductoAdapter { producto ->
            val intent = Intent(this, DetalleProductoActivity::class.java)
            intent.putExtra("id", producto.id)
            intent.putExtra("nombre", producto.nombre)
            intent.putExtra("categoria", producto.categoria)
            intent.putExtra("codigo", producto.codigo)
            intent.putExtra("precio", producto.precio)
            intent.putExtra("cantidad", producto.cantidad)
            intent.putExtra("imagen", producto.imagen)

            startActivity(intent)
        }
        recyclerProductos.adapter = adapter

        // Permite preguntar si desde el ViewModel nos envia Productos (fuente local, Room)
        viewModel.productos.observe(this) { productos ->
            adapter.submitList(productos)
        }

        // GET remoto (API -> SQL Server)
        val apiRepository = ProductoApiRepository(RetrofitClient.create(this))
        apiViewModel = ProductoApiViewModel(apiRepository)

        // Realizamos un evento, al hacer click en el botón agregar se nos abrirá la pantalla detalleProducto
        val newProducto = findViewById<FloatingActionButton>(R.id.btnAgregarProducto)
        newProducto.setOnClickListener {
            val intent = Intent(this, DetalleProducto::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Cada vez que se vuelve a esta pantalla (después de crear, editar o eliminar un producto), se recarga la lista para reflejar el estado actual, ya sea desde la API (online) o Room (offline).
        cargarProductosRemotos()
    }

    private fun cargarProductosRemotos() {
        if (NetworkUtils.hayInternet(this)) {
            // Modo ONLINE: pide los datos a la API
            apiViewModel.obtenerProductos { listaDto ->
                val listaProducto = listaDto.map { it.toEntity() }
                adapter.submitList(listaProducto)

                // Sincroniza: guarda una copia de cada producto en Room,
                // para que el modo offline también refleje lo que existe en la API.
                lifecycleScope.launch {
                    listaProducto.forEach { producto ->
                        viewModel.guardarProducto(producto)
                    }
                }
            }
        } else {
            // Modo OFFLINE: sin conexión, se avisa al usuario.
            // Los datos locales (Room) ya se muestran automáticamente gracias al observer de viewModel.productos.
            Toast.makeText(
                this,
                "Sin conexión. Mostrando datos guardados localmente.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
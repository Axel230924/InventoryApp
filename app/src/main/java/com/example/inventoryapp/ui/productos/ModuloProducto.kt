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
import com.example.inventoryapp.data.remote.dto.toDto
import com.example.inventoryapp.data.repository.ProductoApiRepository
import com.example.inventoryapp.viewmodel.ProductoApiViewModel
import com.example.inventoryapp.data.utils.NetworkUtils
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ModuloProducto : AppCompatActivity() {

    // Las guardamos como propiedades de la clase para poder reutilizarlas en onResume() sin volver a crearlas
    private lateinit var apiViewModel: ProductoApiViewModel
    private lateinit var apiRepository: ProductoApiRepository // NUEVO: necesario para sincronizarPendientes()
    private lateinit var viewModel: ProductoViewModel
    private lateinit var adapter: ProductoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_producto)

        // Nos permite recrear y traer el flujo de la base de datos para poder utilizarlo aquí
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
            intent.putExtra("syncId", producto.syncId)
            intent.putExtra("serverId", producto.serverId ?: 0)
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
        apiRepository = ProductoApiRepository(RetrofitClient.create(this))
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
        cargarProductosRemotos()
    }

    private fun cargarProductosRemotos() {

        if (NetworkUtils.hayInternet(this)) {

            sincronizarPendientes {

                apiViewModel.obtenerProductos { listaDto ->

                    val listaProducto =
                        listaDto.map { it.toEntity() }

                    lifecycleScope.launch {

                        listaProducto.forEach { producto ->

                            viewModel.sincronizarDesdeAzure(producto)
                        }

                        viewModel.eliminarProductosQueYaNoExistenEnAzure(
                            listaProducto
                        )
                    }
                }
            }

        } else {

            Toast.makeText(
                this,
                "Sin conexión. Mostrando datos guardados localmente.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun sincronizarPendientes(onFinish: () -> Unit) {
        lifecycleScope.launch {

            // PRODUCTOS PENDIENTES DE CREAR

            val pendientesCrear =
                viewModel.obtenerPendientesCrear()

            android.util.Log.d(
                "SYNC_DEBUG",
                "Pendientes a crear: ${pendientesCrear.size}"
            )

            for (producto in pendientesCrear) {

                try {

                    val respuesta =
                        apiRepository.guardarProducto(
                            producto.toDto()
                        )

                    val serverId = respuesta.id

                    if (serverId == null) {
                        throw Exception(
                            "Azure no devolvió el ID del producto."
                        )
                    }

                    // Guardamos el ID de Azure y marcamos como sincronizado
                    viewModel.marcarComoSincronizado(
                        producto.syncId,
                        serverId
                    )

                    android.util.Log.d(
                        "SYNC_DEBUG",
                        "Sincronizado con éxito: ${producto.nombre}"
                    )

                } catch (e: Exception) {

                    android.util.Log.e(
                        "SYNC_DEBUG",
                        "Error al sincronizar ${producto.nombre}: ${e.message}"
                    )
                }
            }

            // PRODUCTOS PENDIENTES DE ELIMINAR

            val pendientesEliminar =
                viewModel.obtenerPendientesEliminar()

            android.util.Log.d(
                "SYNC_DEBUG",
                "Pendientes a eliminar: ${pendientesEliminar.size}"
            )

            for (producto in pendientesEliminar) {

                try {

                    val serverId = producto.serverId

                    if (serverId == null) {
                        // Nunca llegó a Azure, solamente se elimina localmente
                        viewModel.eliminarPorId(producto.id)

                        android.util.Log.d(
                            "SYNC_DEBUG",
                            "Eliminado solamente de Room: ${producto.nombre}"
                        )

                        continue
                    }

                    // Eliminar usando el ID de Azure
                    apiRepository.eliminarProducto(serverId)

                    // Después de confirmar el borrado remoto, eliminamos el registro local
                    viewModel.eliminarPorId(producto.id)

                    android.util.Log.d(
                        "SYNC_DEBUG",
                        "Eliminado con éxito: ${producto.nombre}"
                    )

                } catch (e: Exception) {

                    android.util.Log.e(
                        "SYNC_DEBUG",
                        "Error al eliminar ${producto.nombre}: ${e.message}"
                    )
                }
            }

            onFinish()
        }
    }
}
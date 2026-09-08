package com.inventoryapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.inventoryapp.data.entity.Producto   // Importamos la entidad Producto
import com.inventoryapp.data.repository.ProductoRepository   // Importamos el repository

class ProductoViewModel(private val repository: ProductoRepository):ViewModel(){   // Creamos una clase
    fun guardarProducto(producto: Producto)   // Creamos una función guardar producto
    {
        viewModelScope.launch{repository.insertar(producto)}    // accedemos a la función insertar del repository
    }

    fun actualizarProducto(producto: Producto) {
        viewModelScope.launch {
            repository.actualizar(producto)
        }
    }// accedemos a la función actualizar del repository

    val productos = repository.listaProducto  // Toma la lista que traemos desde el repository

    // Paso EXTRA: Agregamos la función para eliminar un producto desde el ViewModel
    fun eliminarProducto(producto: Producto) {
        viewModelScope.launch {
            repository.eliminar(producto)
        }
    }

    // ---- Funciones para sincronización diferida (offline-first) ----

    // Obtiene los productos creados/editados offline, aún no enviados a la API
    suspend fun obtenerPendientesCrear(): List<Producto> {
        return repository.obtenerPendientesCrear()
    }

    // Obtiene los productos marcados para eliminar, pendientes de confirmar en la API
    suspend fun obtenerPendientesEliminar(): List<Producto> {
        return repository.obtenerPendientesEliminar()
    }

    // Marca un producto como sincronizado exitosamente con la API
    fun marcarComoSincronizado(syncId: String, serverId: Int) {
        viewModelScope.launch {
            repository.marcarComoSincronizado(syncId, serverId)
        }
    }

    // Elimina definitivamente un producto de Room, tras confirmar el borrado en la API
    fun eliminarPorId(id: Int) {
        viewModelScope.launch {
            repository.eliminarPorId(id)
        }
    }

    fun sincronizarDesdeAzure(producto: Producto) {
        viewModelScope.launch {
            repository.sincronizarDesdeAzure(producto)
        }
    }
}
package com.example.inventoryapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventoryapp.data.remote.dto.ProductoDto
import com.example.inventoryapp.data.repository.ProductoApiRepository
import kotlinx.coroutines.launch

class ProductoApiViewModel(
    private val repository: ProductoApiRepository
) : ViewModel() {

    var productos: List<ProductoDto> = emptyList()
        private set

    fun obtenerProductos(onResult: (List<ProductoDto>) -> Unit) {
        viewModelScope.launch {
            productos = repository.obtenerProductos()
            onResult(productos)
        }
    }

    fun guardarProducto(producto: ProductoDto) {
        viewModelScope.launch {
            repository.guardarProducto(producto)
        }
    }

    fun actualizarProducto(id: Int, producto: ProductoDto) {
        viewModelScope.launch {
            repository.actualizarProducto(id, producto)
        }
    }

    fun eliminarProducto(id: Int) {
        viewModelScope.launch {
            repository.eliminarProducto(id)
        }
    }
}
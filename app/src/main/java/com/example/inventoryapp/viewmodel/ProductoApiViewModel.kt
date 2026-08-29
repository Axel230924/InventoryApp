package com.example.inventoryapp.viewmodel

import android.util.Log
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
            try {
                productos = repository.obtenerProductos()
                onResult(productos)
            } catch (e: Exception) {
                Log.e("ProductoApiViewModel", "Error al obtener productos: ${e.message}")
            }
        }
    }

    fun guardarProducto(producto: ProductoDto) {
        viewModelScope.launch {
            try {
                repository.guardarProducto(producto)
            } catch (e: Exception) {
                Log.e("ProductoApiViewModel", "Error al guardar producto: ${e.message}")
            }
        }
    }

    fun actualizarProducto(id: Int, producto: ProductoDto) {
        viewModelScope.launch {
            try {
                repository.actualizarProducto(id, producto)
            } catch (e: Exception) {
                Log.e("ProductoApiViewModel", "Error al actualizar producto: ${e.message}")
            }
        }
    }

    fun eliminarProducto(id: Int) {
        viewModelScope.launch {
            try {
                repository.eliminarProducto(id)
            } catch (e: Exception) {
                Log.e("ProductoApiViewModel", "Error al eliminar producto: ${e.message}")
            }
        }
    }
}
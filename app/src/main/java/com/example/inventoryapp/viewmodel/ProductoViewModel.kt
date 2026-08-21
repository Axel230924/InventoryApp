package com.inventoryapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.inventoryapp.data.entity.Producto
import com.inventoryapp.data.repository.ProductoRepository

class ProductoViewModel(private val repository:ProductoRepository):ViewModel(){
    fun guardarProducto(producto: Producto)
    {
    viewModelScope.launch{repository.insertar(producto)}
    }
}
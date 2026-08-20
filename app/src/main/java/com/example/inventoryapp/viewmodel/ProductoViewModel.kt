package com.example.inventoryapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.inventoryapp.data.entity.Producto
import com.example.inventoryapp.data.repository.ProductoRepository

class ProductoViewModel(private val repository:ProductoRepository):ViewModel(){
    fun guardarProducto(producto: Producto)
    {
    viewModelScope.launch{repository.Insertar(producto)}
    }
}
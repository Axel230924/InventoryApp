package com.inventoryapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.inventoryapp.data.entity.Producto   // Importamos la entidad Producto
import com.inventoryapp.data.repository.ProductoRepository   // Inmportamos el repository

class ProductoViewModel(private val repository: ProductoRepository):ViewModel(){   // Creamos una clase
    fun guardarProducto(producto: Producto)   // Creamos una función guardar producto
    {
    viewModelScope.launch{repository.insertar(producto)}    // Ccedemos a la función insertar del repository
    }
}
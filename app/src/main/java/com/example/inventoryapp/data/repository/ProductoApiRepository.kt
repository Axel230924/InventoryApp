package com.example.inventoryapp.data.repository

import com.example.inventoryapp.data.remote.api.ApiService
import com.example.inventoryapp.data.remote.dto.ProductoDto

class ProductoApiRepository(
    private val api: ApiService
) {
    suspend fun obtenerProductos() = api.obtenerProductos()

    suspend fun guardarProducto(producto: ProductoDto) = api.guardarProducto(producto)

    suspend fun actualizarProducto(id: Int, producto: ProductoDto) =
        api.actualizarProducto(id, producto)

    suspend fun eliminarProducto(id: Int) = api.eliminarProducto(id)
}
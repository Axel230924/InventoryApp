package com.example.inventoryapp.data.remote.dto

import com.inventoryapp.data.entity.Producto

data class ProductoDto(
    val id: Int?,
    val syncId: String,
    val nombre: String,
    val precio: Double,
    val cantidad: Int,
    val categoria: String,
    val codigo: String?,
    val imagen: String?
)

// Mapper: convierte lo que llega de la API (DTO) en un Producto de Room
fun ProductoDto.toEntity(): Producto {
    return Producto(
        id = 0,
        syncId = syncId,
        serverId = id,
        nombre = nombre,
        precio = precio,
        cantidad = cantidad,
        categoria = categoria,
        codigo = codigo ?: "",
        imagen = imagen ?: "",
        sincronizado = true
    )
}

// Mapper inverso: convierte un Producto de Room en un DTO para enviarlo a la API
fun Producto.toDto(): ProductoDto {
    return ProductoDto(
        id = serverId,
        syncId = syncId,
        nombre = nombre,
        precio = precio,
        cantidad = cantidad,
        categoria = categoria,
        codigo = codigo,
        imagen = imagen
    )
}
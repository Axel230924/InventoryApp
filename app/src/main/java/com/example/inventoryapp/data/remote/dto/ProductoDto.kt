package com.example.inventoryapp.data.remote.dto

data class ProductoDto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val cantidad: Int,
    val categoria: String,
    val imagen: String?
)
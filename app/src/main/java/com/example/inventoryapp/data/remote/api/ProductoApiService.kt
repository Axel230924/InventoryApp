package com.example.inventoryapp.data.remote.api

import com.example.inventoryapp.data.remote.dto.ProductoDto
import com.example.inventoryapp.data.remote.dto.LoginRequest
import com.example.inventoryapp.data.remote.dto.LoginResponse
import retrofit2.http.*

interface ApiService {

    @GET("api/Productoes")
    suspend fun obtenerProductos(): List<ProductoDto>

    @POST("api/Productoes")
    suspend fun guardarProducto(@Body producto: ProductoDto): ProductoDto

    @PUT("api/Productoes/{id}")
    suspend fun actualizarProducto(
        @Path("id") id: Int,
        @Body producto: ProductoDto
    )

    @DELETE("api/Productoes/{id}")
    suspend fun eliminarProducto(@Path("id") id: Int)

    @POST("api/Auth/Login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
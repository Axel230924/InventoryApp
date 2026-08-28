package com.example.inventoryapp.data.remote.retrofit

import android.content.Context
import com.example.inventoryapp.data.remote.api.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:5053/"

    fun create(context: Context): ApiService {

        // Crea el cliente HTTP que utilizará Retrofit.
        val client = OkHttpClient.Builder()
            .addInterceptor(
                AuthInterceptor(context)
            )
            .build()

        // Configura Retrofit con el cliente HTTP.
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
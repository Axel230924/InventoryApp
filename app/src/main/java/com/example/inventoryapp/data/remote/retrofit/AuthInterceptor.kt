package com.example.inventoryapp.data.remote.retrofit

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        // Obtiene el token JWT almacenado localmente.
        val sharedPreferences = context.getSharedPreferences(
            "InventoryPreferences",
            Context.MODE_PRIVATE
        )

        val token = sharedPreferences.getString("token", null)

        // Agrega el token a la petición si existe.
        val request = chain.request().newBuilder()

        if (!token.isNullOrEmpty()) {
            request.addHeader(
                "Authorization",
                "Bearer $token"
            )

            android.util.Log.d(
                "JWT_INTERCEPTOR",
                "Authorization Bearer agregado correctamente")
        }

        return chain.proceed(request.build())
    }
}
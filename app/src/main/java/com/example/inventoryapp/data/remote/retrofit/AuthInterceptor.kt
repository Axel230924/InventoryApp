package com.example.inventoryapp.data.remote.retrofit

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import com.example.inventoryapp.data.session.SessionManager

class AuthInterceptor(
    private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        // Obtiene el token JWT mediante SessionManager.
        val sessionManager = SessionManager(context)
        val token = sessionManager.obtenerToken()

        // Agrega el token a la petición si existe.
        val request = chain.request().newBuilder()

        if (!token.isNullOrEmpty()) {
            request.addHeader(
                "Authorization",
                "Bearer $token"
            )

            android.util.Log.d(
                "JWT_INTERCEPTOR",
                "Authorization Bearer agregado correctamente"
            )
        }

        return chain.proceed(request.build())
    }
}
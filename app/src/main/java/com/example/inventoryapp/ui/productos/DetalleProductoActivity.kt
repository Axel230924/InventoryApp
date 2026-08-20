package com.example.inventoryapp.ui.productos

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.inventoryapp.R

class DetalleProductoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Activa el diseño de pantalla completa.
        enableEdgeToEdge()

        // Carga el diseño XML de la pantalla Detalle del producto.
        setContentView(R.layout.detalle_producto)

        // Permite adaptar la pantalla a las barras del sistema.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->

            // Obtiene el tamaño de las barras del sistema.
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Aplica el espacio necesario para que los elementos no queden debajo de las barras del sistema.
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            // Devuelve los Insets para continuar el procesamiento.
            insets
        }
    }
}
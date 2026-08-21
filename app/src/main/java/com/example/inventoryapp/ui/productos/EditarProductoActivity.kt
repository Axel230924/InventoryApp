package com.example.inventoryapp.ui.productos

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.inventoryapp.R

class EditarProductoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Activa el diseño de pantalla de detalle producto
        enableEdgeToEdge()

        // Carga la pantalla principal de detalle producto
        setContentView(R.layout.activity_detalle_producto)

        // Permite adaptar la pantalla a las barras del sistema

        setContentView(R.layout.activity_editar_producto)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->

            // Obtiene el tamaño de las barras del sistema
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Aplica el espacio necesario para evitar que los elementos queden debajo de las barras del sistema
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)

            // Devuelve las Insets para continuar el procesamiento
            insets
        }
    }
}
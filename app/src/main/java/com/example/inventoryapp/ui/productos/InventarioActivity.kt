package com.example.inventoryapp.ui.productos

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.inventoryapp.R

class InventarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Activa el diseño de pantalla de inventario
        enableEdgeToEdge()

        // Carga la pantalla principal de Inventario
        setContentView(R.layout.inventario_main)

        // Permite adaptar la pantalla a las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->

            // Obtiene el tamaño de las barras del sistema
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Aplica el espacio necesario para evitar que los elementos queden debajo de las barras del sistema
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)

            // Devuelve las Insets para continuar el procesamiento
            insets

        }

        // Busca la tarjeta de Laptop Dell Inspiro 15.
        val cardMov4 = findViewById<CardView>(R.id.cardMovimiento4)
        cardMov4.setOnClickListener {

            // Detecta cuando el usuario toca la tarjeta.
            // Crea una navegación hacia la pantalla Detalle del producto.
            val intent = Intent(this, DetalleProductoActivity::class.java)


            // Ejecuta la navegación.
            startActivity(intent)
        }
    }
}
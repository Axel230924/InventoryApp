package com.example.inventoryapp

import android.content.Intent // Permite navegar entre Activities.
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.inventoryapp.ui.dashboard.Dashboard

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Activa el diseño de pantalla completa.
        enableEdgeToEdge()

        // Carga la pantalla de inicio/login.
        setContentView(R.layout.activity_main)

        // Ajusta la pantalla a las barras del sistema.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        // Mantiene la aplicación en modo claro.
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO
        )

        // Busca el botón de inicio de sesión.
        val btnInicioS = findViewById<Button>(R.id.btniniciosesion)

        // Al presionar el botón, abre el Dashboard.
        btnInicioS.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)

            startActivity(intent)

            // Cierra la pantalla de inicio para que no se pueda regresar con el botón Atrás.
            finish()
        }
    }
}
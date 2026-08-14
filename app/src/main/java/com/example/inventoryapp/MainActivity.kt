package com.example.inventoryapp

import android.content.Intent // debemos importar la función Intent.
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val btnInicioS = findViewById<Button>(R.id.btniniciosesion) // Selecciona el id del botón

        btnInicioS.setOnClickListener { // Esto indica un evento, al dar click se ejecutará lo siguiente
            val intent = Intent(this, Dashboard::class.java) // Selecciona la actividad.
            startActivity(intent) // Indica que se va a ejecutar.
            finish() // Destruye la pantalla de login para que no pueda retroceder a ella.
        }
    }
}
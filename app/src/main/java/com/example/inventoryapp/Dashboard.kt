package com.example.inventoryapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class Dashboard : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navViews: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        drawerLayout = findViewById(R.id.DrawerLayout)
        navViews = findViewById(R.id.NavigationView)

        val mainConstraint = findViewById<ConstraintLayout>(R.id.mainConstraint)
        val linearLayout = findViewById<LinearLayout>(R.id.linearLayout)

        // RESPONSIVIDAD GENERAL (Dashboard + Menú Lateral)
        ViewCompat.setOnApplyWindowInsetsListener(drawerLayout) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Dashboard: Ajuste de barra azul superior e inferior
            linearLayout.setPadding(
                linearLayout.paddingLeft,
                systemBars.top,
                linearLayout.paddingRight,
                linearLayout.paddingBottom
            )
            mainConstraint.setPadding(0, 0, 0, systemBars.bottom)

            // NavigationView: Empuja el menú hacia abajo de la hora y arriba de los botones
            navViews.setPadding(
                0,
                systemBars.top,    // Evita que el logo quede debajo de la hora
                0,
                systemBars.bottom  // Evita que el botón 'Cerrar sesión' quede bajo los botones/gestos
            )

            insets
        }

        // Eventos del Menú Lateral
        val btnMenu = findViewById<ImageView>(R.id.imgmenu)
        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val btnmenuvolver = findViewById<ImageView>(R.id.imgMenuNV)
        btnmenuvolver.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        val btncerarsesion = findViewById<Button>(R.id.btncerrars)
        btncerarsesion?.setOnClickListener {
            cerrarSesion()
        }
    }

    private fun cerrarSesion() { // funcion de cerrar sesion
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
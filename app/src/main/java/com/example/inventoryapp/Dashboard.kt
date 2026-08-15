package com.example.inventoryapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
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

        // Responsividad del Dashboard y del menú lateral
        ViewCompat.setOnApplyWindowInsetsListener(drawerLayout) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            linearLayout.setPadding(
                linearLayout.paddingLeft,
                systemBars.top,
                linearLayout.paddingRight,
                linearLayout.paddingBottom
            )

            mainConstraint.setPadding(0, 0, 0, systemBars.bottom)

            navViews.setPadding(
                0,
                systemBars.top,
                0,
                systemBars.bottom
            )

            insets
        }

        // Botón abrir menú
        val btnMenu = findViewById<ImageView>(R.id.imgmenu)
        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Botón cerrar menú
        val btnMenuVolver = findViewById<ImageView>(R.id.imgMenuNV)
        btnMenuVolver.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        // Cerrar sesión
        val btnCerrarSesion = findViewById<Button>(R.id.btncerrars)
        btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }

        // ================= MENÚ LATERAL =================

        val cvProducto = findViewById<CardView>(R.id.CardViewProductos)
        cvProducto.setOnClickListener {
            val intent = Intent(this, moduloProducto::class.java)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        val opProducto = findViewById<LinearLayout>(R.id.LinearLayoutProductoNV)
        opProducto.setOnClickListener {
            val intent = Intent(this, moduloProducto::class.java)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        val cvReporte = findViewById<CardView>(R.id.CardViewReportes)
        cvReporte.setOnClickListener {
            val intent = Intent(this, Reportes::class.java)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        val opReporte = findViewById<LinearLayout>(R.id.LinearLayoutReportesNV)
        opReporte.setOnClickListener {
            val intent = Intent(this, Reportes::class.java)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        val cvInventario = findViewById<CardView>(R.id.CardViewInventario)
        cvInventario.setOnClickListener {
            val intent = Intent(this, InventarioActivity::class.java)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        val opInventario = findViewById<LinearLayout>(R.id.LinearLayoutInventarioNV)
        opInventario.setOnClickListener {
            val intent = Intent(this, InventarioActivity::class.java)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        val cvConfiguracion = findViewById<CardView>(R.id.CardViewConfiguracion)
        cvConfiguracion.setOnClickListener {
            val intent = Intent(this, Configuracion::class.java)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        val opConfiguracion = findViewById<LinearLayout>(R.id.LinearLayoutConfiguracionNV)
        opConfiguracion.setOnClickListener {
            val intent = Intent(this, Configuracion::class.java)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    // Función para cerrar sesión
    private fun cerrarSesion() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
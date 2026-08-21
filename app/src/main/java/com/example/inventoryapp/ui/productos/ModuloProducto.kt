package com.example.inventoryapp.ui.productos

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inventoryapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ModuloProducto : AppCompatActivity() {

    private var drawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_producto)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val productoModels = listOf(

            ProductoModel(
                "Laptop Dell inspiron 15",
                "$500",
                R.drawable.laptopinspiro15,
                codigo = "0001",
                cantidad =  10
            ),

            ProductoModel(
                "Mouse Logitech",
                "$25",
                R.drawable.raton,
                codigo = "0002",
                cantidad = 17
            ),

            ProductoModel(
                "Teclado Mecánico",
                "$45",
                R.drawable.teclado,
                codigo = "0003",
                cantidad = 15
            ),

            ProductoModel(
                "Monitor Samsung",
                "$800",
                R.drawable.monitorsamsung,
                codigo = "0004",
                cantidad = 10
            ),

            ProductoModel(
                "Audífonos Sony",
                "$60",
                R.drawable.audifonosony,
                codigo = "0005",
                cantidad = 20
            )
        )

        val recyclerProductos = findViewById<RecyclerView>(R.id.rvProductos)

        recyclerProductos.layoutManager = LinearLayoutManager(this)

        recyclerProductos.adapter = ProductoAdapter(productoModels)

        val newProducto= findViewById<FloatingActionButton>(R.id.btnAgregarProducto)

        newProducto.setOnClickListener {
            val intent= Intent(this, DetalleProducto::class.java)

            startActivity(intent)
        }

        val btnMenuP = findViewById<ImageView>(R.id.imgBtnMenu)
        btnMenuP.setOnClickListener {
            drawerLayout?.openDrawer(GravityCompat.START)
        }
    }

}
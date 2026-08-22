package com.example.inventoryapp.ui.productos

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.inventoryapp.R
import android.widget.TextView
// Importaciones necesarias
import android.content.Intent
import android.widget.Button

class DetalleProductoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Activa el diseño de pantalla completa.
        enableEdgeToEdge()

        // Carga el diseño XML de la pantalla Detalle del producto.
        setContentView(R.layout.detalle_producto)

        val id = intent.getIntExtra("id", -1)

        val nombre = intent.getStringExtra("nombre") ?: ""
        val codigo = intent.getStringExtra("codigo") ?: ""
        val categoria = intent.getStringExtra("categoria") ?: ""
        val precio = intent.getDoubleExtra("precio", 0.0)
        val cantidad = intent.getIntExtra("cantidad", 0)

        val txtNombreProducto = findViewById<TextView>(R.id.tvNombreProducto)

        val txtCodigo = findViewById<TextView>(R.id.tvCodigoProducto)

        val txtCategoria = findViewById<TextView>(R.id.tvCategoriaProducto)

        val txtPrecio = findViewById<TextView>(R.id.tvPrecioProducto)

        val txtcantidad = findViewById<TextView>(R.id.tvCantidad)

        txtNombreProducto.text = nombre

        txtCodigo.text = codigo

        txtCategoria.text = categoria

        txtPrecio.text = "C$${String.format("%.2f", precio)}"

        txtcantidad.text = "$cantidad"

        val btnEditar = findViewById<Button>(R.id.btnEditar)
        btnEditar.setOnClickListener {
            val intent = Intent(this, EditarProductoActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("nombre", nombre)
            intent.putExtra("codigo", codigo)
            intent.putExtra("categoria", categoria)
            intent.putExtra("precio", precio)
            intent.putExtra("cantidad", cantidad)
            startActivity(intent)
        }

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
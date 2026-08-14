package com.example.inventoryapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductoAdapter(
    private val productos: List<Producto>
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val imagen: ImageView = itemView.findViewById(R.id.imgProducto)
        val nombre: TextView = itemView.findViewById(R.id.txtNombreProducto)
        val codigo: TextView = itemView.findViewById(R.id.txtCodigo)
        val stock: TextView = itemView.findViewById(R.id.txtStock)
        val precio: TextView = itemView.findViewById(R.id.txtPrecio)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductoViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)

        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ProductoViewHolder,
        position: Int
    ) {

        val producto = productos[position]

        holder.imagen.setImageResource(producto.imagen)
        holder.nombre.text = producto.nombre
        holder.codigo.text = "Código: ${producto.codigo}"
        holder.stock.text = "Stock: ${producto.stock}"
        holder.precio.text = "${producto.precio}"
    }

    override fun getItemCount(): Int {
        return productos.size
    }
}
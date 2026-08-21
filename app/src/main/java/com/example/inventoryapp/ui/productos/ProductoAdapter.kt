package com.example.inventoryapp.ui.productos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.inventoryapp.R

class ProductoAdapter(
    private val productos: List<ProductoModel>,
    //Actualizar Producto
    private val onClick:(ProductoModel)->Unit


) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    inner class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val imagen: ImageView = itemView.findViewById(R.id.imgProducto)
        val nombre: TextView = itemView.findViewById(R.id.txtNombreProducto)
        val codigo: TextView = itemView.findViewById(R.id.txtCodigo)
        val stock: TextView = itemView.findViewById(R.id.txtStock)
        val precio: TextView = itemView.findViewById(R.id.txtPrecio)


        // FUN BIND correspondiente a actualizar producto
        fun bind(producto: ProductoModel) {
            nombre.text = producto.nombre
            precio.text = producto.precio.toString()
            stock.text = producto.cantidad.toString()

            imagen.setImageResource(producto.imagen)
            codigo.text = "Código: ${producto.codigo}"

            itemView.setOnClickListener {
                onClick(producto)
            }
        }

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
        holder.bind(producto)
    }

    override fun getItemCount(): Int {
        return productos.size
    }

}
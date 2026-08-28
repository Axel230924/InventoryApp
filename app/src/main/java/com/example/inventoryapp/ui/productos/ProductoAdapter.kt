package com.example.inventoryapp.ui.productos

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.inventoryapp.databinding.ItemProductoBinding
import com.inventoryapp.data.entity.Producto

// Adapter encargado de mostrar los productos en el RecyclerView
class ProductoAdapter(
    private val onItemClick: (Producto) -> Unit
) : ListAdapter<Producto, ProductoAdapter.ViewHolder>(
    DiffCallback()
) {

    // ViewHolder encargado de representar cada producto
    class ViewHolder(
        private val binding: ItemProductoBinding,
        private val onProductoClick: (Producto) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        // Vincula los datos del producto con la interfaz
        fun bind(producto: Producto) {

            binding.txtNombreProducto.text = producto.nombre

            binding.txtCodigo.text =
                "Código: ${producto.codigo}"

            binding.txtPrecio.text =
                "C$${String.format("%.2f", producto.precio)}"

            binding.txtCantidad.text =
                "Cantidad: ${producto.cantidad}"

            // Mostrar imagen si existe
            if (producto.imagen.isNotEmpty()) {

                binding.imgProducto.setImageURI(
                    Uri.parse(producto.imagen)
                )
            }

            // Detectar click sobre el producto
            binding.root.setOnClickListener {

                onProductoClick(producto)
            }
        }
    }

    // Crear el ViewHolder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding =
            ItemProductoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return ViewHolder(
            binding,
            onItemClick
        )
    }

    // Vincular el producto con el ViewHolder
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        holder.bind(
            getItem(position)
        )
    }

    // Comparar los productos
    class DiffCallback :
        DiffUtil.ItemCallback<Producto>() {

        // Determinar si es el mismo producto
        override fun areItemsTheSame(
            oldItem: Producto,
            newItem: Producto
        ): Boolean {

            return oldItem.id == newItem.id
        }

        // Determinar si cambiaron los datos
        override fun areContentsTheSame(
            oldItem: Producto,
            newItem: Producto
        ): Boolean {

            return oldItem == newItem
        }
    }

}
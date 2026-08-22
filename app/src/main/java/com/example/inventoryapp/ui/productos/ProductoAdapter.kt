package com.example.inventoryapp.ui.productos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView  // Importamos el recyclerview
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter   // Le indicamos que podemos trabajar el recyclerView como lista
import com.inventoryapp.data.entity.Producto   // Importar la entidad
import com.example.inventoryapp.databinding.ItemProductoBinding  // Importar el binding
import android.net.Uri

// Creamos la clase ProductoAdapter que trabajará con la entidad Producto
class ProductoAdapter(
    private val onProductoClick: (Producto) -> Unit  // Significa: Cuando alguien de click en producto se ejecutará una función.
) :
    ListAdapter<Producto, ProductoAdapter.ViewHolder>(  // Indica que cada lista Producto será representado como un ViewHolder
        DiffCallback()  // Permite comparar las listas y detectar si cambió algo.
    ) {

    // Indica que trabajará con una fila individual del recyclerView y cada ViewHolder será una copia de itemProducto
    class ViewHolder(
        private val binding: ItemProductoBinding,  // Permite acceder directamente a las vistas de item_producto.xml mediante View Binding.
        private val onProductoClick: (Producto) -> Unit  // Al hacer click en producto se ejecutará esta función
    ) : RecyclerView.ViewHolder(binding.root) {

        // Esta función nos permite trabajar en conjunto con el recyclerView y con la entidad Producto
        fun bind(producto: Producto) {

            // Le asignamos al elemento Nombre del recycler el atributo nombre del producto.
            // Aquí nos permite unir la entidad con el recycler
            binding.txtNombreProducto.text = producto.nombre
            binding.txtCodigo.text = "Código: ${producto.codigo}"
            binding.txtPrecio.text = "C$${String.format("%.2f", producto.precio)}"
            binding.txtCantidad.text = "Cantidad: ${producto.cantidad}"
            if (producto.imagen.isNotEmpty()) {
                binding.imgProducto.setImageURI(
                    Uri.parse(producto.imagen)
                )
            }
            binding.root.setOnClickListener {
                onProductoClick(producto)  // Llamamos a que se ejecute la función
            }
        }
    }

    // Esta función nos permite crear la vista de cada producto dentro del itemProducto
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        // Aquí le indicamos que cargue el diseño xml ItemProducto
        val binding = ItemProductoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        // Nos retorna un ViewHolder
        return ViewHolder(binding, onProductoClick) // Retornamos el bindin y la función.
    }

    // Esta función nos permite colocar los datos que traemos de la entidad y los colocamos en un ViewHolder
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        // Toma la posición y la pasa al bind
        holder.bind(
            getItem(position)
        )
    }

    // Esta clase se crea para permitir hacer validaciones.
    class DiffCallback :
        DiffUtil.ItemCallback<Producto>() {

        // Primera validación ¿el producto nuevo y el anterior representan al mismo producto?
        // Si ambos tienen el mismo id, entonces es el mismo producto
        override fun areItemsTheSame(
            oldItem: Producto,
            newItem: Producto
        ): Boolean {

            return oldItem.id == newItem.id
        }

        // Aquí pregunta ¿Tienen los mismos datos?
        // Si hay un cambió entonces listAdapter tiene que actualizarse.
        override fun areContentsTheSame(
            oldItem: Producto,
            newItem: Producto
        ): Boolean {

            return oldItem == newItem
        }
    }

}
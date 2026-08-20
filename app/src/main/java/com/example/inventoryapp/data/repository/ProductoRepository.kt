package com.inventoryapp.data.repository

import com.example.inventoryapp.data.dao.ProductoDao   // Importamos la clase ProductoDao
import com.inventoryapp.data.entity.Producto   // Importamos el modelo Producto

class ProductoRepository(   // Creamos una clase
    private val dao: ProductoDao   // Hacemos una instancia a la clase ProductoDao y lo guardamos en una variable
) {
    suspend fun insertar(   // Creamos una función insertar para posteriormente llamarla
        producto: Producto   // Hacemos la instancia con Producto
    ){
        dao.Insertar(producto)   // Accedemos a la función insertar en ProductoDao
    }
    suspend fun actualizar(   // Creamos una función actualizar para posteriormente llamarla
        producto: Producto   // Hacemos la instancia con Producto
    ){
        dao.Actualizar(producto)   // Accedemos a la función actualizar en ProductoDao
    }
    suspend fun eliminar(   // Creamos una función eliminar para posteriormente llamarla
        producto: Producto   // Hacemos la instancia con Producto
    ){
        dao.Eliminar(producto)   // Accedemos a la función eliminar en ProductoDao
    }
    suspend fun obtenerTodos():   // Creamos una función obtener todos productos para posteriormente llamarla
            List<Producto>{   //Listamos todos los productos que tenemos.
        return dao.ObtenerTodos()   // Accedemos a la función obtenertodos en ProductoDao
    }
}

package com.inventoryapp.data.repository

import com.inventoryapp.data.dao.ProductoDao   // Importamos la clase ProductoDao
import com.inventoryapp.data.entity.Producto   // Importamos el modelo Producto

class ProductoRepository(   // Creamos una clase
    private val dao: ProductoDao   // Hacemos una instancia a la clase ProductoDao y lo guardamos en una variable
) {

    val listaProducto = dao.ObtenerTodos() // Llamamos a la función ObtenerTodo del dao.
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

    // Nuevas funciones para sincronización diferida (offline-first)
    // Obtiene todos los productos que fueron creados o modificados mientras el dispositivo estaba sin conexión a Internet, y que por lo tanto todavía no se han enviado a la API (SQL Server). Se usan para reintentar la sincronización cuando vuelve la conexión.
    suspend fun obtenerPendientesCrear(): List<Producto> {
        return dao.obtenerPendientesCrear()
    }

    // Obtiene todos los productos que el usuario eliminó mientras no había  conexión. En vez de borrarlos de Room de inmediato, quedan marcados como "pendientes de eliminar" hasta poder confirmar el borrado en la API.
    suspend fun obtenerPendientesEliminar(): List<Producto> {
        return dao.obtenerPendientesEliminar()
    }

    // Marca un producto como sincronizado exitosamente con la API. Se llama después de que el reintento de creación/actualización remota tuvo éxito, para que ese producto deje de aparecer en la lista de pendientes.
    suspend fun marcarComoSincronizado(id: Int) {
        dao.marcarComoSincronizado(id)
    }

    // Elimina definitivamente un producto de la base de datos local (Room). Se llama después de confirmar que el DELETE remoto (hacia la API) se completó con éxito, cerrando el ciclo de eliminación diferida.
    suspend fun eliminarPorId(id: Int) {
        dao.eliminarPorId(id)
    }
}


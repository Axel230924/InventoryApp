package com.inventoryapp.data.dao
import androidx.lifecycle.LiveData
import androidx.room.Dao   // Importamos la base de datos con el recurso DAO
import androidx.room.Delete   // Importamos la consulta Delete
import androidx.room.Insert  // Importamos la consulta Insert
import androidx.room.Query   // Importamos la consulta Query
import androidx.room.Update   // Importamos la consulta Update
import com.inventoryapp.data.entity.Producto   // Importamos la entidad Producto
@Dao
interface ProductoDao {   // Creamos el DAO (Consultas a la base de datos)
    @Insert suspend fun Insertar(producto: Producto)   // Función con la consulta Insertar

    @Update suspend fun Actualizar(producto: Producto)   // Función con la consulta Actualizar

    @Delete suspend fun Eliminar(producto: Producto)   // Función con la consulta Eliminar

    @Query("SELECT * FROM Productos ORDER BY id DESC") fun ObtenerTodos(): LiveData<List<Producto>>   // Función listar
}
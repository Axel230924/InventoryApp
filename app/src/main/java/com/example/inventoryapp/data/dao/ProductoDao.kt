package com.inventoryapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.inventoryapp.data.entity.Producto

@Dao
interface ProductoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun Insertar(producto: Producto)

    @Update suspend fun Actualizar(producto: Producto)

    @Delete suspend fun Eliminar(producto: Producto)

    @Query("SELECT * FROM Productos WHERE pendienteEliminar = 0 ORDER BY id DESC")
    fun ObtenerTodos(): LiveData<List<Producto>>

    @Query("SELECT * FROM productos WHERE sincronizado = 0 AND pendienteEliminar = 0")
    suspend fun obtenerPendientesCrear(): List<Producto>

    @Query("SELECT * FROM productos WHERE pendienteEliminar = 1")
    suspend fun obtenerPendientesEliminar(): List<Producto>

    @Query("SELECT * FROM productos WHERE sincronizado = 0 AND pendienteEliminar = 0")
    suspend fun obtenerPendientesSincronizacion(): List<Producto>

    @Query("SELECT * FROM productos WHERE syncId = :syncId LIMIT 1")
    suspend fun obtenerPorSyncId(syncId: String): Producto?

    @Query("UPDATE productos SET sincronizado = 1, serverId = :serverId WHERE syncId = :syncId")
    suspend fun marcarComoSincronizado(syncId: String, serverId: Int)

    @Query("DELETE FROM productos WHERE id = :id")
    suspend fun eliminarPorId(id: Int)

    @Query("SELECT * FROM productos WHERE sincronizado = 1 AND pendienteEliminar = 0")
    suspend fun obtenerProductosSincronizados(): List<Producto>
}
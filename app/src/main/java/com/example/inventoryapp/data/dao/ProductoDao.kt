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

    @Query("SELECT * FROM Productos ORDER BY id DESC") fun ObtenerTodos(): LiveData<List<Producto>>
}
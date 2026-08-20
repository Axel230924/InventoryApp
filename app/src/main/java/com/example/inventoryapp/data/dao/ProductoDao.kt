package com.example.inventoryapp.data.dao
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.inventoryapp.data.entity.Producto

@Dao
interface ProductoDao {
    @Insert suspend fun Insertar(producto: Producto)

    @Update suspend fun Actualizar(producto: Producto)

    @Delete suspend fun Eliminar(producto: Producto)

    @Query("SELECT * FROM Productos") suspend fun ObtenerTodos(): List<Producto>
}
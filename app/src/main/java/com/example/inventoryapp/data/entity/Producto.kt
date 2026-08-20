package com.inventoryapp.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val nombre:String,
    val precio:Double,
    val cantidad:Int,
    val categoria:String,
    val imagen:String=""
)

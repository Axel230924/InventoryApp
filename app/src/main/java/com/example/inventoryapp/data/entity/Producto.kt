package com.inventoryapp.data.entity
import androidx.room.Entity // Importamos la entidad de la base de datos
import androidx.room.PrimaryKey // Importamos el atributo Llave primaria

@Entity(tableName = "productos") // Definimos el nombre de nuestra tabla como una entidad
data class Producto( // creamos una clase
    @PrimaryKey(autoGenerate = true) // Le indicamos que la Llave primaria va a ser autoincrementable
    val id:Int = 0, // Atributo Llave primaria
    val nombre:String, // Nombre
    val precio:Double, // Precio
    val cantidad:Int, // Cantidad
    val categoria:String, // Categoria a la que pertenece
    val imagen:String="" // Campo imagen
)

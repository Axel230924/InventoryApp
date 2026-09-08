package com.inventoryapp.data.entity
import androidx.room.Entity // Importamos la entidad de la base de datos
import androidx.room.PrimaryKey // Importamos el atributo Llave primaria
import java.util.UUID

@Entity(tableName = "productos") // Definimos el nombre de nuestra tabla como una entidad
data class Producto( // creamos una clase
    @PrimaryKey(autoGenerate = true) // Le indicamos que la Llave primaria va a ser autoincrementable
    val id:Int = 0, // Atributo Llave primaria
    val syncId: String = UUID.randomUUID().toString(),
    val serverId: Int? = null,
    val nombre:String, // Nombre
    val precio:Double, // Precio
    val cantidad:Int, // Cantidad
    val categoria:String, // Categoria a la que pertenece
    val codigo: String,
    val imagen:String="", // Campo imagen
    val sincronizado: Boolean = false, // false = pendiente de crear o actualizar en la API
    val pendienteEliminar: Boolean = false // true = hay que borrarlo de la API cuando vuelve la conexión
)
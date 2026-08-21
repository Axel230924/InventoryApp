package com.inventoryapp.data.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.inventoryapp.data.dao.ProductoDao
import com.inventoryapp.data.entity.Producto

@Database(
    entities = [Producto::class],
    version = 1
)
abstract class InventoryDatabase :
    RoomDatabase() {
    abstract fun productoDao():
            ProductoDao
    companion object {
        @Volatile
        private var INSTANCE:
                InventoryDatabase? = null
        fun getDatabase(
            context: Context
        ): InventoryDatabase {
            return INSTANCE ?: synchronized(this){
                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        InventoryDatabase::class.java,
                        "inventory_db"
                    ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

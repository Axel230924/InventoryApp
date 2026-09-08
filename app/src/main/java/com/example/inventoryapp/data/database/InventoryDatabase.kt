package com.inventoryapp.data.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.inventoryapp.data.dao.ProductoDao
import com.inventoryapp.data.entity.Producto

@Database(
    entities = [Producto::class],
    version = 3
)
abstract class InventoryDatabase :
    RoomDatabase() {
    abstract fun productoDao():
            ProductoDao
    companion object {
        @Volatile
        private var INSTANCE:
                InventoryDatabase? = null

        // Migración de la versión 1 a la 2: agrega las columnas nuevas
        // sin borrar los datos existentes.
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE productos ADD COLUMN sincronizado INTEGER NOT NULL DEFAULT 1"
                )
                db.execSQL(
                    "ALTER TABLE productos ADD COLUMN pendienteEliminar INTEGER NOT NULL DEFAULT 0"
                )
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {

                db.execSQL(
                    "ALTER TABLE productos ADD COLUMN syncId TEXT NOT NULL DEFAULT ''"
                )

                db.execSQL(
                    "ALTER TABLE productos ADD COLUMN serverId INTEGER"
                )
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DELETE FROM productos")
            }
        }

        fun getDatabase(
            context: Context
        ): InventoryDatabase {
            return INSTANCE ?: synchronized(this){
                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        InventoryDatabase::class.java,
                        "inventory_db"
                    )
                        .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                        .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

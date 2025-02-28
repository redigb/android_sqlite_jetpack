package com.redrd.sqlite_rd.database_sqlite

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.redrd.sqlite_rd.database_sqlite.dao.ContactoDao
import com.redrd.sqlite_rd.database_sqlite.entitie.Contactos


@Database(entities = [Contactos::class], version = 2, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun contactDao(): ContactoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "contactos_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

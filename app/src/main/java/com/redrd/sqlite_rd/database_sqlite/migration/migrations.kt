package com.redrd.sqlite_rd.database_sqlite.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Realizamos la migración, en este caso agregamos una nueva columna
        database.execSQL("ALTER TABLE contactos_table ADD COLUMN newColumn TEXT")
    }
}
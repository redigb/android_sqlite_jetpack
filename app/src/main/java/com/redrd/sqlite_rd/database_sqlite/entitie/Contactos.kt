package com.redrd.sqlite_rd.database_sqlite.entitie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "contactos_table")
data class Contactos(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,
    @ColumnInfo(name = "nombre")
    val nombre: String,
    @ColumnInfo(name = "numero")
    val numero: String,
   /* @ColumnInfo(name = "update")
    var update: Date?*/
)
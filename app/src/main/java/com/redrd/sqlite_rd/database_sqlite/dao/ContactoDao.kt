package com.redrd.sqlite_rd.database_sqlite.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.redrd.sqlite_rd.database_sqlite.entitie.Contactos

@Dao
interface ContactoDao {
    @Query("SELECT * FROM contactos_table WHERE id = :id")
    suspend fun getContactById(id: Int): Contactos?

    @Query("SELECT * FROM contactos_table")
    suspend fun getAllContacts(): List<Contactos>

    @Insert
    suspend fun insert(contact: Contactos)

    @Update
    suspend fun update(contact: Contactos)

    @Delete
    suspend fun delete(contact: Contactos)
}
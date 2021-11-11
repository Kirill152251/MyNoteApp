package com.example.mynoteapp

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM note_table ORDER by id ASC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("DELETE FROM note_table")
    suspend fun deleteAllNotes()
}
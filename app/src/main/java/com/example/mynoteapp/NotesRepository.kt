package com.example.mynoteapp

import androidx.lifecycle.LiveData

class NotesRepository(private val noteDAO: NoteDAO) {

    val allNotes: LiveData<List<Note>> = noteDAO.getAllNotes()

    suspend fun insert(note: Note) {
        noteDAO.insert(note)
    }

    suspend fun delete(note: Note) {
        noteDAO.delete(note)
    }

    suspend fun update(note: Note) {
        noteDAO.update(note)
    }

    suspend fun deleteAllNotes() {
        noteDAO.deleteAllNotes()
    }
}
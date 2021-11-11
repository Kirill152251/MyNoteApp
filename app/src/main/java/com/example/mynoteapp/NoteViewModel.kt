package com.example.mynoteapp

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class NoteViewModel(application: Application): AndroidViewModel(application){

     var allNotes: LiveData<List<Note>>
     var repository: NotesRepository


     init {
         val dao = NotesDatabase.getDatabase(application).getNotesDao()
         repository = NotesRepository(dao)
         allNotes = repository.allNotes
     }

    fun insert(note: Note) = viewModelScope.launch {
        repository.insert(note)
    }

    fun update(note: Note) = viewModelScope.launch {
        repository.update(note)
    }

    fun delete(note: Note) = viewModelScope.launch {
        repository.delete(note)
    }

    fun deleteAllNotes() = viewModelScope.launch {
        repository.deleteAllNotes()
    }

}
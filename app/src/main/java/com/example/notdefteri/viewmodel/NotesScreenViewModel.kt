package com.example.notdefteri.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notdefteri.models.Notes
import com.example.notdefteri.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotesScreenViewModel : ViewModel() {
    private val repository = NoteRepository()
    val notes = repository.notesFlow

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun addNote(title: String, description: String) {
        viewModelScope.launch {
            try {
                repository.createNote(title, description)
            } catch (e: Exception) {
                _errorMessage.value = "Error adding note: ${e.message}"
            }
        }
    }

    fun updateNote(note: Notes) {
        viewModelScope.launch {
            try {
                repository.updateNote(note)
            } catch (e: Exception) {
                _errorMessage.value = "Error updating note: ${e.message}"
            }
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            try {

                repository.deleteNote(noteId)
            } catch (e: Exception) {
                _errorMessage.value = "Error deleting note: ${e.message}"
                Log.e("NotesScreenViewModel", "Error deleting note: ${e.message}")
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
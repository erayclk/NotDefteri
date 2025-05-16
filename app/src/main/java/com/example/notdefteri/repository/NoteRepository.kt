package com.example.notdefteri.repository

import android.util.Log
import com.example.notdefteri.models.Notes
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class NoteRepository {

    val db = FirebaseFirestore.getInstance()
    val notesDBRef = db.collection("notes")

    private val _notesFlow = MutableStateFlow<List<Notes>>(emptyList())
    val notesFlow = _notesFlow.asStateFlow()

    init {
        setupRealTimeListener()
    }

    private fun setupRealTimeListener() {
        notesDBRef.addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("NoteRepository", "Error in real-time listener: ${error.message}")
                return@addSnapshotListener
            }

            value?.let { snapshot ->
                val notes = snapshot.documents.mapNotNull { doc ->
                    try {
                        doc.toObject(Notes::class.java)?.apply {
                            id = doc.id // Ensure ID is set from document
                        }
                    } catch (e: Exception) {
                        Log.e("NoteRepository", "Error converting document: ${e.message}")
                        null
                    }
                }
                Log.d("NoteRepository", "Received ${notes.size} notes from Firestore")
                _notesFlow.value = notes
            }
        }
    }

    // Yeni bir not eklemek için ID oluşturur
    suspend fun createNote(title: String, description: String): Notes {
        try {
            val documentRef = notesDBRef.document()
            val noteId = documentRef.id
            Log.d("NoteRepository", "Creating new note with ID: $noteId")
            
            val note = Notes(
                id = noteId,
                title = title,
                description = description
            )
            
            documentRef.set(note).await()
            Log.d("NoteRepository", "Note created successfully with ID: $noteId")
            return note
        } catch (e: Exception) {
            Log.e("NoteRepository", "Error creating note: ${e.message}")
            throw e
        }
    }

    // Not güncelleme
    suspend fun updateNote(note: Notes) {
        try {
            Log.d("NoteRepository", "Attempting to update note with ID: ${note.id}")
            val documentRef = notesDBRef.document(note.id)
            
            documentRef.get().await().let { document ->
                if (document.exists()) {
                    documentRef.set(note).await()
                    Log.d("NoteRepository", "Note successfully updated with ID: ${note.id}")
                } else {
                    Log.e("NoteRepository", "Note with ID ${note.id} does not exist")
                    throw Exception("Note with ID ${note.id} does not exist")
                }
            }
        } catch (e: Exception) {
            Log.e("NoteRepository", "Error updating note: ${e.message}")
            throw e
        }
    }

    // Not silme
    suspend fun deleteNote(noteId: String) {
        try {
            Log.d("NoteRepository", "Attempting to delete note with ID: $noteId")
            val documentRef = notesDBRef.document(noteId)
            documentRef.get().await().let { document ->
                if (document.exists()) {
                    documentRef.delete().await()
                    Log.d("NoteRepository", "Note successfully deleted with ID: $noteId")
                } else {
                    Log.e("NoteRepository", "Note with ID $noteId does not exist")
                    throw Exception("Note with ID $noteId does not exist")
                }
            }
        } catch (e: Exception) {
            Log.e("NoteRepository", "Error deleting note: ${e.message}")
            throw e
        }
    }

}
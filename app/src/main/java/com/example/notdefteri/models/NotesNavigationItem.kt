package com.example.notdefteri.models

sealed class NotesNavigationItem(val route:String) {
    object Home:NotesNavigationItem("notes_list")
    object Add:NotesNavigationItem("create_note")
}
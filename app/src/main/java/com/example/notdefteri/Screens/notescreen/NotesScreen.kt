package com.example.notdefteri.Screens.notescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.notdefteri.R
import com.example.notdefteri.models.Notes
import com.example.notdefteri.models.NotesNavigationItem
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun NotesScreen(navController: NavController) {

    val db = FirebaseFirestore.getInstance()
    val notesDBRef=db.collection("notes")
    val noteslist = remember { mutableStateListOf<Notes>() }

    LaunchedEffect(Unit) {
        notesDBRef.addSnapshotListener{
            value,error->
            if (error ==null){
                val data = value?.toObjects(Notes::class.java)
                noteslist.clear()
                if (data != null) {
                    noteslist.addAll(data)
                }
            }

        }


    }


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                contentColor = Color.White,
                containerColor = Color.Red,
                shape = CircleShape,
                onClick = { navController.navigate(
                    NotesNavigationItem.Add.route+"/{id}") }) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(it)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.notes_screen_title),
                    fontSize = 40.sp,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.SansSerif,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier
                        .padding(16.dp)

                )
                LazyColumn(modifier = Modifier.padding(16.dp)) {

                    items(noteslist) { notes ->
                        ListItems(notes ,notesDBRef,navController)
                        Spacer(Modifier.padding(8.dp))


                    }
                }


            }

        }
    }
}


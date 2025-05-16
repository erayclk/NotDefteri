package com.example.notdefteri.Screens.notescreen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.notdefteri.models.Notes
import com.example.notdefteri.viewmodel.NotesScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListItems(notes: Notes, navController: NavController) {
    val viewModel: NotesScreenViewModel = viewModel()
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearErrorMessage()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(color = Color.DarkGray)
            .padding(10.dp)
    ) {
        DropdownMenu(
            modifier = Modifier.background(Color.White),
            properties = PopupProperties(clippingEnabled = true),
            offset = DpOffset(x = 200.dp, y = (-120).dp),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Update") },
                onClick = {
                    navController.navigate("update_notes_screen/${notes.id}")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Delete") },
                onClick = {
                    Log.v("Delete", "${notes.id}-+${notes.title}+-${notes.description}")
                    viewModel.deleteNote(notes.id)
                    expanded = false
                }
            )
        }

        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "more vert icon",
            tint = Color.White,
            modifier = Modifier
                .align(alignment = Alignment.TopEnd)
                .clickable { expanded = !expanded }
        )

        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = notes.title,
                style = TextStyle(color = Color.White)
            )
            Text(
                text = notes.description,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                color = Color.White
            )
        }
    }
}
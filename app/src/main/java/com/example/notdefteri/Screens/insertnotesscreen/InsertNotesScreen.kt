package com.example.notdefteri.Screens.insertnotesscreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.notdefteri.R
import com.example.notdefteri.models.Notes
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun InsertNotesScreen(navController: NavController, id: String?) {

    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val notesDBRef = db.collection("notes")

    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }

    val war = stringResource(R.string.warring).toString()

    LaunchedEffect(Unit) {
        if (id != "defaultId") {
            notesDBRef.document(id.toString()).get().addOnSuccessListener {
                val singleData = it.toObject(Notes::class.java)
                title.value = singleData?.title ?: ""
                description.value = singleData?.description ?: ""
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                contentColor = Color.White,
                containerColor = Color.Red,
                shape = CircleShape,
                onClick = {

                    if (title.value.isEmpty() && description.value.isEmpty()) {
                        Toast.makeText(context, war, Toast.LENGTH_SHORT).show()
                    } else {
                        val myNotesId = if (id != "defaultId") {
                            id.toString()
                        } else {
                            notesDBRef.document().id
                        }

                        val notes = Notes(
                            id = myNotesId,
                            title = title.value,
                            description = description.value
                        )
                        notesDBRef.document(myNotesId).set(notes).addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(context, "Başarılı", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            } else {
                                Toast.makeText(context, "Başarısız", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            ) {
                Icon(Icons.Filled.Done, contentDescription = "Check")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.Black)
        )
        {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.insert_data),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                TextField(
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.DarkGray,
                        unfocusedIndicatorColor = Color.DarkGray,
                        focusedTextColor = Color.Transparent,
                        unfocusedTextColor = Color.Transparent,
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    label = {
                        Text(
                            text = stringResource(R.string.enter_title),
                            style = TextStyle(fontSize = 16.sp, color = Color.Black),
                            fontFamily = FontFamily.SansSerif
                        )
                    },
                    value = title.value,
                    onValueChange = { title.value = it},
                    textStyle = TextStyle(textAlign = TextAlign.Start, color = Color.Black),
                    maxLines = 1

                    )
                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.DarkGray,
                        unfocusedIndicatorColor = Color.DarkGray,
                        focusedTextColor = Color.Transparent,
                        unfocusedTextColor = Color.Transparent,
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f),
                    label = {
                        Text(
                            text = stringResource(R.string.enter_discription),
                            style = TextStyle(fontSize = 16.sp, color = Color.Black),
                            fontFamily = FontFamily.SansSerif
                        )
                    },
                    value = description.value,
                    onValueChange = { description.value = it},
                    textStyle = TextStyle(textAlign = TextAlign.Start, color = Color.Black),

                    )




            }


        }
    }

}
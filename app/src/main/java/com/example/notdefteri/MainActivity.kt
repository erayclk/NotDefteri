package com.example.notdefteri

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.notdefteri.Screens.insertnotesscreen.InsertNotesScreen
import com.example.notdefteri.Screens.notescreen.NotesScreen
import com.example.notdefteri.models.NotesNavigationItem

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()


        setContent {
            val rememberNavController = rememberNavController()
            NotesNavigation(rememberNavController)
        }


    }
}

@Composable
fun NotesNavigation(navHostController: NavHostController) {


    NavHost(navController = navHostController, startDestination = NotesNavigationItem.Home.route) {
        composable(NotesNavigationItem.Home.route){
            NotesScreen(navHostController)
        }
        composable(NotesNavigationItem.Add.route+"/{id}"){
            val id = it.arguments?.getString("id")
            InsertNotesScreen(navHostController, id)


        }
        composable("update_notes_screen/{id}") {
            val id = it.arguments?.getString("id")
            InsertNotesScreen(navHostController, id)
        }
    }

}
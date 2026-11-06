package com.example.campsiteapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.campsiteapp.data.DeviceIDs
import com.example.campsiteapp.data.ProfileDatabase
import com.example.campsiteapp.ui.theme.CampsiteAppTheme

/**
 * Activity that shows [ProfileScreen].
 */

class ProfileActivity : ComponentActivity() {
    override fun onCreate( savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            CampsiteAppTheme {
                ProfileScreen(navController, "", mutableListOf<String>("Entrant"), null)
            }
        }
    }
}
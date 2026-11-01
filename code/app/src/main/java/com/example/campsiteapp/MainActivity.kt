package com.example.campsiteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.campsiteapp.controller.OwnerCampsiteController
import com.example.campsiteapp.data.CampsitesDatabase
import com.example.campsiteapp.ui.theme.CampsiteAppTheme
import com.example.campsiteapp.view.OwnerCampsiteListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val campsitesDatabase = CampsitesDatabase()

        setContent {
            CampsiteAppTheme {
                OwnerCampsiteListScreen().CampsitesList(
                    campsites = campsitesDatabase.campsites,
                    onAddCampsite = {campsitesDatabase.addCampsite(it)}
                )
            }
        }
    }
}


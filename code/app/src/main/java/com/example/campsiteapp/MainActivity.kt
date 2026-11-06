package com.example.campsiteapp

import OwnerHomeScreen
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.example.campsiteapp.data.CampsitesDatabase
import com.example.campsiteapp.ui.theme.CampsiteAppTheme
import com.example.campsiteapp.model.Campsite

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val campsitesDatabase = CampsitesDatabase()

        setContent {
            CampsiteAppTheme {
                OwnerHomeScreen (
                    campsites = campsitesDatabase.campsites,
                    onAddCampsite = {campsite -> campsitesDatabase.addCampsite(campsite)},
                    onEditCampsite = {oldCampsiteDetails, newCampsiteDetails ->
                        campsitesDatabase.updateCampsite(
                            oldCampsiteDetails,
                            newCampsiteDetails = newCampsiteDetails
                        )
                    }
                )
            }
        }
    }
}


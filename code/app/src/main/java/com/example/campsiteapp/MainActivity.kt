package com.example.campsiteapp

import OwnerHomeScreen
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.campsiteapp.controller.EntrantWaitlistController


import com.example.campsiteapp.view.EntrantHomeScreen
import com.example.campsiteapp.data.DeviceIDs
import com.example.campsiteapp.data.LotteryDatabase
import com.example.campsiteapp.model.Campsite
import com.example.campsiteapp.model.Lottery

import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.example.campsiteapp.data.CampsitesDatabase
import com.example.campsiteapp.data.DeviceIDs
import com.example.campsiteapp.ui.theme.CampsiteAppTheme
import com.example.campsiteapp.model.Campsite
import com.example.campsiteapp.view.EntrantHomeScreen

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //check deviceIDs class for usage documentation
        deviceIds = DeviceIDs(applicationContext) // Do not delete IMPORTANT(device id)

        startActivity(Intent(this, EntrantHomeScreen::class.java))
        finish()
    }
}
        enableEdgeToEdge()

        val campsitesDatabase = CampsitesDatabase()
        lateinit var deviceIds: DeviceIDs // Do not delete IMPORTANT(device id)

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

                deviceIds = DeviceIDs(applicationContext) // Do not delete IMPORTANT(device id)

                startActivity(Intent(this, EntrantHomeScreen::class.java))
                finish()
            }
        }
    }
}


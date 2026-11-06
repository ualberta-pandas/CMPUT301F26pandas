package com.example.campsiteapp

import android.content.Intent
import OwnerHomeScreen
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi


import com.example.campsiteapp.view.EntrantHomeScreen
import com.example.campsiteapp.data.DeviceIDs
import com.example.campsiteapp.model.CampsitesDatabase
import com.example.campsiteapp.ui.theme.CampsiteAppTheme


class MainActivity : ComponentActivity() {
    private lateinit var deviceIds: DeviceIDs // Do not delete IMPORTANT(device id)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

                //check deviceIDs class for usage documentation
        deviceIds = DeviceIDs(applicationContext) // Do not delete IMPORTANT(device id)

        setContent {
            val campsitesDatabase = CampsitesDatabase()


            CampsiteAppTheme {

                startActivity(Intent(this, EntrantHomeScreen::class.java))


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

                finish()
            }
        }
    }


}
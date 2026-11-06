package com.example.campsiteapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.campsiteapp.controller.OwnerCampsiteController
import com.example.campsiteapp.data.CampsitesDatabase
import com.example.campsiteapp.ui.theme.CampsiteAppTheme
import com.example.campsiteapp.view.OwnerCampsiteListScreen
import com.example.campsiteapp.view.ProfileScreen


import com.example.campsiteapp.data.Campsite
import com.example.campsiteapp.model.CampsitesDatabase

import com.example.campsiteapp.data.Lottery
import com.example.campsiteapp.model.LotteryDatabase

import com.example.campsiteapp.view.EntrantHomeScreen
import com.example.campsiteapp.data.DeviceIDs




class MainActivity : ComponentActivity() {
    private lateinit var deviceIds: DeviceIDs // Do not delete IMPORTANT(device id)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampsiteAppTheme {

                val navController = rememberNavController()
                HomeScreen(navController,{ navController.navigate("Profile") }, { navController.navigate("Modes" )},
                    { navController.navigate("Notifications") })
            }
        }
    }

        //check deviceIDs class for usage documentation
        deviceIds = DeviceIDs(applicationContext) // Do not delete IMPORTANT(device id)
        startActivity(Intent(this, EntrantHomeScreen::class.java))
        finish()
    }


}





package com.example.campsiteapp.view


import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import com.example.campsiteapp.data.CampsitesDatabase
import com.example.campsiteapp.data.DeviceIDs
import com.example.campsiteapp.model.ReservationDatabase
import com.example.campsiteapp.ui.theme.CampsiteAppTheme

class OwnerActivity: ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val campsitesDatabase = CampsitesDatabase()
        val reservationDatabase = ReservationDatabase()



        setContent {

            val deviceId by produceState(initialValue = "") {
                value = DeviceIDs(this@OwnerActivity).fetchDeviceId()?:""
            }

            CampsiteAppTheme {
                OwnerHomeScreen(campsitesDatabase, onBack = {finish()},
                    deviceId = deviceId
                    )
            }
        }
    }
}
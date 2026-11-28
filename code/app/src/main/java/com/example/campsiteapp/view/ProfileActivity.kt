package com.example.campsiteapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
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
            val context = LocalContext.current
            val initialDeviceId = intent.getStringExtra("DEVICE_ID")
            val role = intent.getStringExtra("ROLE")
            val targetProfileDeviceId = intent.getStringExtra("TARGET_DEVICE_ID")

            val deviceId by produceState<String?>(null, context) {
                    val deviceIds = DeviceIDs(context)
                    value = initialDeviceId?: deviceIds.fetchDeviceId() ?: deviceIds.registerDeviceId()
            }
            if (deviceId!=null){
                CampsiteAppTheme {
                    ProfileScreen(deviceId?:"", role, targetProfileDeviceId, { finish() })
                }
            }
        }
    }
}
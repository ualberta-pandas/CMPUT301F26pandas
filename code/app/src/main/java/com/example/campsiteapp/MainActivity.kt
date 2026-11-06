package com.example.campsiteapp

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
import com.example.campsiteapp.controller.OwnerCampsiteController
import com.example.campsiteapp.data.CampsitesDatabase
import com.example.campsiteapp.ui.theme.CampsiteAppTheme
import com.example.campsiteapp.view.OwnerCampsiteListScreen
import com.example.campsiteapp.view.OwnerCampsiteDetailsScreen
import com.example.campsiteapp.data.DeviceIDs
class MainActivity : ComponentActivity() {


    private lateinit var deviceIds: DeviceIDs // Do not delete IMPORTANT(device id)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //check deviceIDs class for usage documentation
        deviceIds = DeviceIDs(applicationContext) // Do not delete IMPORTANT(device id)

        val campsitesDatabase = CampsitesDatabase()
        enableEdgeToEdge()

        setContent {
            CampsiteAppTheme {



            }
        }

//         Uncomment to show the admin screens
//         setContent {
//             CampsiteAppTheme {
//                 com.example.campsiteapp.view.AdminApp(campsitesDatabase)
//             }
//         }
    }
}


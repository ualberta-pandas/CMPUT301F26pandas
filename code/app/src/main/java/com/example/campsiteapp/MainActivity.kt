package com.example.campsiteapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import com.example.campsiteapp.view.EntrantHomeScreen
import com.example.campsiteapp.data.DeviceIDs

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : ComponentActivity() {
    private lateinit var deviceIds: DeviceIDs // Do not delete IMPORTANT(device id)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //check deviceIDs class for usage documentation
        deviceIds = DeviceIDs(applicationContext) // Do not delete IMPORTANT(device id)

        startActivity(Intent(this, EntrantHomeScreen::class.java))
        finish()
    }
}

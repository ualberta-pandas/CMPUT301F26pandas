package com.example.campsiteapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import com.example.campsiteapp.controller.PoolingSystem
import com.example.campsiteapp.view.EntrantHomeScreen
import com.example.campsiteapp.data.DeviceIDs

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : ComponentActivity() {
    private lateinit var deviceIds: DeviceIDs // Do not delete IMPORTANT(device id)

    /**
        Initializes the project. Checks for any lotteries that have exceeded their termination/end
        date, then starts the activity.
     **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //check deviceIDs class for usage documentation
        deviceIds = DeviceIDs(applicationContext) // Do not delete IMPORTANT(device id)
        PoolingSystem.getTerminatedLotteries()

        startActivity(Intent(this, EntrantHomeScreen::class.java))
        finish()
    }
}

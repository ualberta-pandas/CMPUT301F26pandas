package com.example.campsiteapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity


import com.example.campsiteapp.data.Campsite
import com.example.campsiteapp.model.CampsitesDatabase

import com.example.campsiteapp.data.Lottery
import com.example.campsiteapp.model.LotteryDatabase

import com.example.campsiteapp.view.EntrantHomeScreen
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, EntrantHomeScreen::class.java))
        finish()
    }


}



package com.example.campsiteapp.controller

import android.content.Context
import android.content.Intent
import com.example.campsiteapp.CampsiteDatabase
import com.example.campsiteapp.data.Campsite
import com.example.campsiteapp.ui.lottery.CampsiteDetailsScreen

object EntrantCampsiteController {
    fun getCampsiteById(id: String): Campsite? {
        return CampsiteDatabase.getCampsiteById(id)
    }

    fun toggleCampsiteSearch(context: Context, userSearch: String){
        val intent = Intent(context, CampsiteDetailsScreen::class.java)
        intent.putExtra("search", userSearch)
        context.startActivity(intent)
    }

    fun toggleCampsiteListScreen(context: Context){

    }

    fun viewCampsiteDetails(context: Context, campsiteId: String){
        val intent = Intent(context, CampsiteDetailsScreen::class.java)
        intent.putExtra("campsiteId", campsiteId)
        context.startActivity(intent)
    }
}
package com.example.campsiteapp.controller

import android.content.Context
import android.content.Intent
import com.example.campsiteapp.data.Lottery

import com.example.campsiteapp.view.CampsiteDetailsScreen
import com.example.campsiteapp.view.CampsiteListScreen


object EntrantCampsiteController {

    /**
     Returns a list of the lotteries that have their campsites matching
     the user result. Probably going to be done by some regex
     **/
    fun getResultsBySearch(searchResult: String): ArrayList<Lottery>{
        return ArrayList()
    }

    fun toggleSearchResultsScreen(context: Context, userSearch: String){
        val intent = Intent(context, CampsiteListScreen::class.java)
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
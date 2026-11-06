package com.example.campsiteapp.controller

import android.content.Context
import android.content.Intent
import com.example.campsiteapp.data.Lottery
import com.example.campsiteapp.model.LotteryDatabase

import com.example.campsiteapp.view.CampsiteDetailsScreen
import com.example.campsiteapp.view.CampsiteListScreen


object EntrantCampsiteController {

    private val lotteryDB = LotteryDatabase()
    private val lotteries: List<Lottery> = lotteryDB.lotteries


    /**
     Returns a list of the lotteries that have their campsites matching
     the user result. Probably going to be done by some regex
     **/
    fun getResultsBySearch(searchResult: String): List<Lottery>{
        val regex = Regex(Regex.escape(searchResult), RegexOption.IGNORE_CASE)

        val newLotteries = mutableListOf<Lottery>()

        for (lottery in lotteries) {
            if (regex.containsMatchIn(lottery.campsite.name)) {
                newLotteries.add(lottery)
            }
        }

        return newLotteries
    }

    fun toggleSearchResultsScreen(context: Context, userSearch: String){
        val intent = Intent(context, CampsiteListScreen::class.java)
        intent.putExtra("search", userSearch)
        context.startActivity(intent)
    }

    fun toggleCampsiteDetailsScreen(context: Context, lotteryId: String){
        val intent = Intent(context, CampsiteDetailsScreen::class.java)

        var lottery: Lottery? = lotteries.firstOrNull{ it.id == lotteryId }

        if (lottery == null){
            return Unit
        }

        intent.putExtra("lottery", lottery)
        intent.putExtra("campsite", lottery.campsite)
        context.startActivity(intent)
    }
}
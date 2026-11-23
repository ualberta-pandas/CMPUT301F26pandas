package com.example.campsiteapp.controller

import android.content.Context
import android.content.Intent
import com.example.campsiteapp.data.LotteryDatabase
import com.example.campsiteapp.data.Reservation
import com.example.campsiteapp.model.Lottery
import com.example.campsiteapp.model.ReservationDatabase
import com.example.campsiteapp.view.EntrantOfferScreen

object EntrantOfferController {
    private val lotteryDB by lazy { LotteryDatabase() }
    private val reservationDB by lazy { ReservationDatabase() }

    fun toggleOfferScreen(context: Context, lottery: Lottery, hasOffer: Boolean) {
        val intent = Intent(context, EntrantOfferScreen::class.java).apply {
            putExtra("hasOffer", hasOffer)
            putExtra("lottery", lottery)
        }
        context.startActivity(intent)
    }

    /**
        Creates a reservation, and then redirects the user to the home screen
     **/
    fun entrantAcceptOfferHandler(deviceId: String, lottery: Lottery){
    }

    /**
        Removes the user from the waitlist
     */
    fun entrantRejectOfferHandler(deviceId: String){

    }
}
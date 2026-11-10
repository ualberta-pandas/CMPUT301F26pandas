package com.example.campsiteapp.controller

import android.content.Context
import android.content.Intent
import com.example.campsiteapp.model.Lottery
import com.example.campsiteapp.data.Reservation
import com.example.campsiteapp.data.LotteryDatabase
import com.example.campsiteapp.model.ReservationDatabase
import com.example.campsiteapp.view.AdminActivity

import com.example.campsiteapp.view.CampsiteDetailsScreen
import com.example.campsiteapp.view.CampsiteListScreen
import com.example.campsiteapp.view.ProfileActivity
import com.example.campsiteapp.view.OwnerHomeScreen

object EntrantCampsiteController {

    private val lotteryDB by lazy { LotteryDatabase() }
    private val reservationDB by lazy { ReservationDatabase() }

    private val lotteries: List<Lottery> get() = lotteryDB.lotteries
    private val reservations: List<Reservation> get() = reservationDB.reservations


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

    fun getLotteriesById(deviceId: String): List<Lottery>{
        val newLotteries = mutableListOf<Lottery>()


        for (lottery in lotteries) {
            val lotteryInId: String? = lottery.waitlist.firstOrNull { it == deviceId }

            if (lotteryInId != null) {
                newLotteries.add(lottery)
            }
        }
        return newLotteries
    }


    fun getReservationsById(deviceId: String): List<Reservation> {
        val newReservations = mutableListOf<Reservation>()

        for (reservation in reservations) {
            if (reservation.entrantId == deviceId) {
                newReservations.add(reservation)
            }
        }
        return newReservations
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

    fun toggleAdminScreen(context: Context) {
        context.startActivity(Intent(context, AdminActivity::class.java))
    }

    fun toggleProfileScreen(context: Context, deviceId: String = "") {
        val intent = Intent(context, ProfileActivity::class.java)
        context.startActivity(intent)
    }

    fun toggleOwnerScreen(context: Context) {
        val intent = Intent(context, OwnerHomeScreen::class.java)
        context.startActivity(intent)
    }
}
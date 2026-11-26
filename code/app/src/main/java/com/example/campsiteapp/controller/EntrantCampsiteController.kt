package com.example.campsiteapp.controller


import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.campsiteapp.model.Lottery
import com.example.campsiteapp.model.Reservation
import com.example.campsiteapp.data.LotteryDatabase
import com.example.campsiteapp.model.Quad
import com.example.campsiteapp.model.ReservationDatabase
import com.example.campsiteapp.view.AdminActivity

import com.example.campsiteapp.view.CampsiteDetailsScreen
import com.example.campsiteapp.view.CampsiteListScreen
import com.example.campsiteapp.view.OwnerActivity
import com.example.campsiteapp.view.EntrantHomeScreen
import com.example.campsiteapp.view.ProfileActivity


object EntrantCampsiteController {

    private val lotteryDB by lazy { LotteryDatabase() }
    private val reservationDB by lazy { ReservationDatabase() }

    private val lotteries: List<Lottery> get() = lotteryDB.lotteries
    private val reservations: List<Reservation> get() = reservationDB.reservations


    /**
     Returns a list of the lotteries that have their campsites matching
     the user result. Probably going to be done by some regex
     **/
    fun getResultsBySearch(searchResult: Quad<String, String, String, String>): List<Lottery>{
        val campName = Regex(Regex.escape(searchResult.first), RegexOption.IGNORE_CASE)
        val location = Regex(Regex.escape(searchResult.second), RegexOption.IGNORE_CASE)

        val newLotteries = mutableListOf<Lottery>()

        for (lottery in lotteries) {
            if (campName.containsMatchIn(lottery.campsite.name) ||
                location.containsMatchIn(lottery.campsite.location)||
                searchResult.third == lottery.campsite.startDate+'-'+lottery.campsite.endDate
                || lottery.campsite.occupancy >=searchResult.fourth.toInt() )  {
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

    fun toggleSearchResultsScreen(context: Context, userSearch: Quad<String,String,String, String>){
        val intent = Intent(context, CampsiteListScreen::class.java)
        intent.putExtra("search", userSearch.first)
        intent.putExtra("location", userSearch.second)
        intent.putExtra("dates", userSearch.third)
        intent.putExtra("guests", userSearch.second)
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

        context.startActivity(Intent(context, OwnerActivity::class.java))

    }

    fun toggleMenuScreen(context: Context) {
        val intent = Intent(context, EntrantHomeScreen::class.java)
        context.startActivity(intent)
    }
}
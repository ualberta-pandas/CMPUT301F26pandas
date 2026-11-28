package com.example.campsiteapp.controller


import android.content.Context
import android.content.Intent
import com.example.campsiteapp.data.CampsitesDatabase
import com.example.campsiteapp.model.Lottery
import com.example.campsiteapp.data.LotteryDatabase
import com.example.campsiteapp.model.Campsite
import com.example.campsiteapp.model.Quad
import com.example.campsiteapp.model.Reservation
import com.example.campsiteapp.model.ReservationDatabase
import com.example.campsiteapp.view.AdminActivity

import com.example.campsiteapp.view.CampsiteDetailsScreen
import com.example.campsiteapp.view.CampsiteListScreen
import com.example.campsiteapp.view.OwnerActivity
import com.example.campsiteapp.view.EntrantHomeScreen
import com.example.campsiteapp.view.ProfileActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter


object EntrantCampsiteController {

    private val lotteryDB by lazy { LotteryDatabase() }
    private val reservationDB by lazy { ReservationDatabase() }

    private val lotteries: List<Lottery> get() = lotteryDB.lotteries

    private val campsites: List<Campsite> get() = CampsitesDatabase().campsites
    private val reservations: List<Reservation> get() = reservationDB.reservations


    /**
    Returns a list of the lotteries that have their campsites matching
    the user result.
     **/
    fun getResultsBySearch(searchResult: Quad<String, String, String, String>): List<Campsite> {
        val (name, location, dates, occupancy) = searchResult
        val minOccupancy = occupancy.toIntOrNull()

        val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd") // the one format

        // search dates -> fmt
        val parts = dates.split(" - ")
        val from = if (parts.size == 2) parts[0].trim().split("/").let {
            LocalDate.of(it[2].toInt(), it[1].toInt(), it[0].toInt()).format(fmt)
        } else null
        val to = if (parts.size == 2) parts[1].trim().split("/").let {
            LocalDate.of(it[2].toInt(), it[1].toInt(), it[0].toInt()).format(fmt)
        } else null

        return campsites.filter { camp ->
            // database dates -> fmt
            val campStart = LocalDate.parse(camp.startDate).format(fmt)
            val campEnd = LocalDate.parse(camp.endDate).format(fmt)

            (name.isBlank() || camp.name.contains(name, ignoreCase = true)) &&
                    (location.isBlank() || camp.location.contains(location, ignoreCase = true)) &&
                    (from == null || to == null || (campStart <= to && campEnd >= from)) &&
                    (minOccupancy == null || camp.occupancy >= minOccupancy)
        }
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

    fun toggleSearchResultsScreen(context: Context, userSearch: Quad<String, String, String, String>){
        val intent = Intent(context, CampsiteListScreen::class.java)
        intent.putExtra("search", userSearch.first)
        intent.putExtra("location", userSearch.second)
        intent.putExtra("dates", userSearch.third)
        intent.putExtra("guests", userSearch.fourth)
        context.startActivity(intent)
    }

    fun toggleCampsiteDetailsScreen(context: Context, lotteryId: String){
        val intent = Intent(context, CampsiteDetailsScreen::class.java)

        val lottery: Lottery? = lotteries.firstOrNull{ it.id == lotteryId }

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
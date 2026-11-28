package com.example.campsiteapp.controller

import com.example.campsiteapp.model.Reservation
import com.example.campsiteapp.model.ReservationDatabase
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class OwnerReservationsController {

    private val reservationDB by lazy { ReservationDatabase() }
    private val reservations: List<Reservation> get() = reservationDB.reservations


    fun getReservationsById(deviceId: String): List<Reservation> {
        val newReservations = mutableListOf<Reservation>()

        for (reservation in reservations) {
            if (reservation.campsite.ownerId == deviceId && reservation.status.displayText=="Accepted") {

                newReservations.add(reservation)
            }
        }
        return newReservations
    }

    fun getPastReservationsById(deviceId: String): List<Reservation> {
        val newReservations = mutableListOf<Reservation>()

        for (reservation in reservations) {
            if (reservation.campsite.ownerId == deviceId && reservation.endDate.isNotEmpty()) {

                val end = LocalDate.parse(reservation.endDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"))

                if(end.isBefore(LocalDate.now()) && reservation.status.displayText=="Completed"){
                    newReservations.add(reservation)
                }

            }
        }
        return newReservations
    }

    fun getCancelledReservationsById(deviceId: String): List<Reservation> {
        val newReservations = mutableListOf<Reservation>()

        for (reservation in reservations) {
            if (reservation.campsite.ownerId == deviceId && reservation.status.displayText=="Cancelled") {

                newReservations.add(reservation)
            }
        }
        return newReservations
    }



}
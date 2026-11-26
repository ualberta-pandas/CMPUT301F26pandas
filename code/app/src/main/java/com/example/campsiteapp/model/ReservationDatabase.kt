package com.example.campsiteapp.model

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.example.campsiteapp.model.Reservation
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class ReservationDatabase {

    private val db = Firebase.firestore
    private val reservationsRef = db.collection("reservations")
    private val _reservations = mutableStateListOf<Reservation>()

    init {
        reservationsRef.addSnapshotListener { snapshots, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            val newList = snapshots?.documents?.mapNotNull { document ->
                try {
                    document.toObject(Reservation::class.java)
                        ?.copy(id = document.id)
                } catch (e: Exception) {
                    Log.e(
                        "ReservationDatabase",
                        "Skipping bad reservation ${document.id}: ${e.message}"
                    )
                    null
                }
            } ?: emptyList()

            _reservations.clear()
            _reservations.addAll(newList)

        }
    }

    val reservations: List<Reservation>
        get() = _reservations

    fun addReservation(reservation: Reservation) {
        reservationsRef.add(
            mapOf(
                "id" to reservation.id,
                "entrantId" to reservation.entrantId,
                "startDate" to reservation.startDate,
                "endDate" to reservation.endDate,
                "status" to reservation.status
            )
        )
    }

    fun updateStatus(id: String, newStatus: String) {
        reservationsRef.document(id).update("status", newStatus)
    }

    fun deleteReservation(id: String) {
        reservationsRef.document(id).delete()
    }
}
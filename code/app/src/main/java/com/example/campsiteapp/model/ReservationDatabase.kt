package com.example.campsiteapp.model

import androidx.compose.runtime.mutableStateListOf
import com.example.campsiteapp.data.Reservation
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

            _reservations.clear()

            snapshots?.documents?.forEach { document ->
                val reservation = document.toObject(Reservation::class.java)
                if (reservation != null) {
                    _reservations.add(reservation.copy(id = document.id))
                }
            }
        }
    }

    val reservations: List<Reservation>
        get() = _reservations

    fun addReservation(reservation: Reservation) {
        reservationsRef.add(
            mapOf(
                "campsiteId" to reservation.campsiteId,
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
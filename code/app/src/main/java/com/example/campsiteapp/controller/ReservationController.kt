/*
 * Provides an entrant's past reservations from the Firestore "reservations"
 * collection.
 *
 * Outstanding issues:
 * - Active reservations, offers and cancelling are not implemented yet.
 * - Queries Firestore directly until a ReservationDatabase exists.
 */
package com.example.campsiteapp.controller

import androidx.compose.runtime.mutableStateListOf
import com.example.campsiteapp.model.Reservation
import com.example.campsiteapp.model.ReservationStatus
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

/**
 * Loads the past reservations of one entrant and keeps them up to date.
 *
 * @param entrantId device ID of the entrant whose reservations are loaded.
 */
class ReservationController(entrantId: String) {
    private val _pastReservations = mutableStateListOf<Reservation>()

    /** The entrant's completed, cancelled, rejected and expired reservations. */
    val pastReservations: List<Reservation>
        get() = _pastReservations

    init {
        Firebase.firestore.collection("reservations")
            .whereEqualTo("entrantId", entrantId)
            .whereIn("status", ReservationStatus.entries.map { it.name })
            .addSnapshotListener { snapshots, error ->
                if (error != null) return@addSnapshotListener
                _pastReservations.clear()
                snapshots?.documents?.forEach { document ->
                    document.toObject(Reservation::class.java)?.let {
                        _pastReservations.add(it.copy(id = document.id))
                    }
                }
            }
    }
}

/*
 * Provides an entrant's past reservations from the Firestore "reservations"
 * collection.
 *
 * Outstanding issues:
 * - Active reservations, offers and cancelling are not implemented yet.
 * - Queries Firestore directly until a ReservationDatabase exists.
 */
package com.example.campsiteapp.controller

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.example.campsiteapp.model.Reservation
import com.example.campsiteapp.model.ReservationStatus
import com.example.campsiteapp.view.AdminActivity
import com.example.campsiteapp.view.PastReservationsScreen
import com.example.campsiteapp.view.PastReservationsView
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Loads the past reservations of one entrant and keeps them up to date.
 *
 * @param entrantId device ID of the entrant whose reservations are loaded.
 */
class ReservationController(entrantId: String) {

    init {
        completeFinishedReservations(entrantId)
    }
    private val _pastReservations = mutableStateListOf<Reservation>()

    /** The entrant's completed, cancelled, rejected and expired reservations. */
    val pastReservations: List<Reservation>
        get() = _pastReservations

    val pastStatuses = listOf(
        ReservationStatus.COMPLETED,
        ReservationStatus.CANCELLED,
        ReservationStatus.REJECTED,
        ReservationStatus.EXPIRED
    ).map { it.name }

    init {
        Firebase.firestore.collection("reservations")
            .whereEqualTo("entrantId", entrantId)
            .whereIn("status", pastStatuses)
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

    private val openStatuses = listOf(
        ReservationStatus.ACCEPTED
    ).map { it.name }

    private fun completeFinishedReservations(entrantId: String) {
        val db = Firebase.firestore
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        db.collection("reservations")
            .whereEqualTo("entrantId", entrantId)
            .get()
            .addOnSuccessListener { snapshots ->
                val now = LocalDate.now()
                val batch = db.batch()
                var changed = 0

                for (document in snapshots.documents) {
                    val status = document.getString("status")

                    val endDate = try {
                        document.getString("endDate")?.let { LocalDate.parse(it, formatter) }
                    } catch (e: DateTimeParseException) {
                        null
                    }

                    if (status in openStatuses && endDate != null && endDate.isBefore(now)) {
                        batch.update(
                            document.reference,
                            "status",
                            ReservationStatus.COMPLETED.name
                        )
                        changed++
                    }
                }

                if (changed > 0) {
                    batch.commit().addOnFailureListener { e ->
                        Log.e("ReservationController", "Could not complete reservations", e)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("ReservationController", "Could not check reservations", e)
            }
    }

    fun togglePastReservationsScreen(context: Context) {
        context.startActivity(Intent(context, PastReservationsScreen::class.java))
    }
}

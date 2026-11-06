/*
 * An entrant's reservation of a campsite.
 *
 * Outstanding issues:
 * - Nothing creates reservations yet.
 * - Whether the entrant acknowledged the campsite rules is not tracked yet.
 */
package com.example.campsiteapp.data

/**
 * A reservation of [campsite] by an entrant for the booked dates.
 *
 * @property entrantId device ID of the entrant who made the reservation.
 * @property startDate first booked day, as dd/MM/yyyy.
 * @property endDate last booked day, as dd/MM/yyyy.
 */
data class Reservation(
    val id: String = "",
    val campsite: Campsite = Campsite(),
    val entrantId: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val status: ReservationStatus = ReservationStatus.COMPLETED,
)

/**
 * State of a reservation once it is no longer active.
 *
 * @property displayText text shown to the entrant.
 */
enum class ReservationStatus(val displayText: String) {
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    REJECTED("Rejected by host"),
    EXPIRED("Offer expired"),
}

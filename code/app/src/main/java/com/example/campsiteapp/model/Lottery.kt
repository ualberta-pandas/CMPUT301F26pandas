package com.example.campsiteapp.model

import java.time.LocalDate

/**
 * A lottery an owner can run for a campsite
 *
 * @property id Firestore document id
 * @property ownerId Owner who created lottery
 * @property campsiteId Campsite lottery is for
 * @property bookingStartDate First date of booking period being raffled
 * @property bookingEndDate Last date of booking period being raffled
 * @property lotteryStartDate First date entrants may join lottery
 * @property lotteryEndDate Last date entrants may join lottery
 * @property numWinners Number of lottery winners that may book during booking period
 * @property waitlistCapacity Max number of entrants allowed to join lottery
 * @property status Where lottery currently is (open, drawing, closed)
 */
data class Lottery(
    val id: String = "",
    val ownerId: String = "",
    val campsiteId: String = "",
    var bookingStartDate: Long = 0L,
    var bookingEndDate: Long = 0L,
    var lotteryStartDate: Long = 0L,
    var lotteryEndDate: Long = 0L,
    var numWinners: Int,
    var waitlistCapacity: Int,
    var status: String = ""
) {

}
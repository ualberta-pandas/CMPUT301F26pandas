package com.example.campsiteapp.model

/**
 * A lottery an owner can run for a campsite
 *
 * Dates are text in yyyy-mm-dd format. Empty means "not set yet"
 *
 * @property id Firestore document id
 * @property ownerId Owner who created lottery
 * @property campsiteIds Campsites lottery is for
 * @property bookingStartDate First date of booking period being raffled
 * @property bookingEndDate Last date of booking period being raffled
 * @property lotteryStartDate First date entrants may join lottery
 * @property lotteryEndDate Last date entrants may join lottery
 * @property numWinners Number of lottery winners that may book during booking period
 * @property waitlistMax Max number of entrants allowed to join lottery
 * @property status Where lottery currently is (open, drawing, closed)
 */
data class Lottery(
    val id: String = "",
    val ownerId: String = "",
    val campsiteIds: List<String> = emptyList(),
    var campsite: Campsite = Campsite(),
    var bookingStartDate: Long = 0L,
    var bookingEndDate: Long = 0L,
    var lotteryStartDate: Long = 0L,
    var lotteryEndDate: Long = 0L,
    var numWinners: Int,
    var waitlistMax: Int = 0,
    var waitlist: ArrayList<String> = ArrayList(),
    var status: String = ""
) : java.io.Serializable {

    fun isFull(): Boolean {
        return waitlist.size == waitlistMax
    }

    fun getCapacityLeft(): Int {
        return waitlistMax - waitlist.size
    }
}
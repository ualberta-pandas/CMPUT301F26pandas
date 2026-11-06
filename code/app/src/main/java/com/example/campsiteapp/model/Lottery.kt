package com.example.campsiteapp.model

/**
 * A lottery an owner can run for a campsite
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
    var bookingStartDate: String = "",
    var bookingEndDate: String = "",
    var lotteryStartDate: String = "",
    var lotteryEndDate: String = "",
    var numWinners: Int = 0,
    var waitlistMax: Int = 0, //null means unlimited
    var waitlist: ArrayList<String> = ArrayList(),
    var status: String = "",
    var winner: String = ""
) : java.io.Serializable {

    fun isFull(): Boolean {
        return waitlist.size == waitlistMax
    }

    fun getCapacityLeft(): Int {
        return waitlistMax - waitlist.size
    }
}
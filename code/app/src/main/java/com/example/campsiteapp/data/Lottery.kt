package com.example.campsiteapp.data

data class Lottery(
    val id: String = "",
    val campsiteId: Int = 0,
    var numWinners: Int = 0,
    var lotteryStartDate: String = "",
    var lotteryEndDate: String = "",
    var waitlistMax: Int = 0,
    var waitlist: ArrayList<String> = ArrayList(),
    var campsite: Campsite = Campsite()
) : java.io.Serializable {

    fun isFull(): Boolean {
        return waitlist.size == waitlistMax
    }

    fun getCapacityLeft(): Int {
        return waitlistMax - waitlist.size
    }
}
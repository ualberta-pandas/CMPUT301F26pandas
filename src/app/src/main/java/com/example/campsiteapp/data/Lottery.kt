package com.example.campsiteapp.data

import java.time.LocalDate

data class Lottery(
    val campsiteId: Int,
    var numWinners: Int,
    var lotteryStartDate: String, var lotteryEndDate: String,

    var waitlistCount: Int, var waitlistMax: Int,
    var waitlist: ArrayList<String>,

    var campsite: Campsite
): java.io.Serializable
{
    fun isFull(): Boolean{
        return waitlistCount == waitlistMax
    }
}

package com.example.campsiteapp.data

import java.time.LocalDate

data class Lottery(
    val campsiteId: Int,
    var numWinners: Int,
    var lotteryStartDate: LocalDate, var lotteryEndDate: LocalDate,

    var waitlistCount: Int, var waitlistMax: Int,
    var waitlist: ArrayList<String>,

    var campsite: Campsite?
){
    fun isFull(): Boolean{
        return waitlistCount == waitlistMax
    }
}

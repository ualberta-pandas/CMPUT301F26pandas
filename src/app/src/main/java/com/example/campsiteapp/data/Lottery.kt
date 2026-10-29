package com.example.campsiteapp.data

import java.time.LocalDate


data class Lottery(
    var campsite: Campsite,
    var numWinners: Int,

    var lotteryStartDate: LocalDate, var lotteryEndDate: LocalDate,

    var waitlistCount: Int,
    var waitlistMax: Int,
    var waitlist: ArrayList<String> // wait list will include the id's of devices
)

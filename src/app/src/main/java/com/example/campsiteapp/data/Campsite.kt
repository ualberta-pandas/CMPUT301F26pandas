package com.example.campsiteapp.data

import java.time.LocalDate

data class Campsite(
    var owner: String, var location: String,
    var startDate: LocalDate, var endDate: LocalDate,
    var rating: Float,
    var description: String,

    var lottery: Lottery?
)


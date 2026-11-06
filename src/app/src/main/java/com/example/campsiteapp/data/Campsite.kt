package com.example.campsiteapp.data

import java.time.LocalDate

data class Campsite(
    val id: String,
    var name: String,
    var location: String,
    var occupancy: Double,
    var owner: String,
    val latitude: String,
    val longitude: String,
    val price: String,
    val startDate: String,
    val endDate: String,
    val description: String = "",
    val tags: String,
    val rules: String,
    val image: String,
    val status: String,
    var rating: Double = 0.0,
    var address: String
): java.io.Serializable
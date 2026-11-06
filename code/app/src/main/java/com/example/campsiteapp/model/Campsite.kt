package com.example.campsiteapp.model

data class Campsite(
    val id: String = "",
    var name: String = "",
    var location: String = "",
    var occupancy: Double = 0.0,
    var owner: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val price: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val description: String = "",
    val tags: String = "",
    val rules: String = "",
    val image: String = "",
    val status: String = "",
    var rating: Double = 0.0,
    var address: String = ""
) : java.io.Serializable
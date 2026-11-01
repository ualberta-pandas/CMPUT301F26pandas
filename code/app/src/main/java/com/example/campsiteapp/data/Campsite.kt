package com.example.campsiteapp.data

data class Campsite(
    val id: String = "",
    var name: String = "",
    var location: String = "",
    var occupancy:String = "",
    var owner: String = "",
    val mapLocation: String = "",
    val price: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val description: String = "",
    val tags: String = "",
    val rules: String = "",
    val image: String = ""
)
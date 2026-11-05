package com.example.campsiteapp.model

/**

    * This data class works like an object and is used to store campsite data
    to the database.

    * Campsite has id, name, location, occupancy, owner, mapLocation,price,
      startDate, endDate, description, tags and image; which are attributes
      of every campsite



 */



data class Campsite(
    val id: String = "",
    var name: String = "",
    var location: String = "",
    var occupancy:String = "",
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
    val status: String = ""
)
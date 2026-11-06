package com.example.campsiteapp.data

data class Reservation(
    val id: String = "",
    val campsiteId: Campsite,
    val entrantId: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val status: String = "pending",
    val campsite: Campsite
): java.io.Serializable
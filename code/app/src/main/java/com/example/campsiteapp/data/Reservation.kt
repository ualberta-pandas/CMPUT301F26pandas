package com.example.campsiteapp.data

import com.example.campsiteapp.model.Campsite

data class Reservation(
    val id: String = "",
    val campsiteId:String =  "",
    val entrantId: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val status: String = "pending",
    val campsite: Campsite
): java.io.Serializable
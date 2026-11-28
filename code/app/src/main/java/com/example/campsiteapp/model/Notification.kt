package com.example.campsiteapp.model

data class Notification(
        val receiver: String = "",
        val sender: String = "",
        val dateSent: String ="",
        val description: String = "",
        val id: String = ""
        )
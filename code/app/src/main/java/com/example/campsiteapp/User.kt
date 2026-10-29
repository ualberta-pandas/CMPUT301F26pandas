package com.example.campsiteapp

import android.content.Context
import android.media.Image

open class User (
    var userName: String,
    var userEmail: String,
    var userPhoneNumber: String,
    var userProfileImage: Image?, //Profile photo is not required
) {
    val userDeviceId = getID()
    var notificationPreference: Boolean = true //True is "notifications on", which is default

    fun getID(): String{
        //To actually generate a unique device identifier, will implement later
        return "Test"
    }
}
package com.example.campsiteapp

import android.content.Context
import android.media.Image
import com.google.firebase.firestore.DocumentId

open class User () {
    var userName: String = ""
    var userEmail: String = ""
    var userPhoneNumber: String = ""
    var userProfileImage: String = "" //Profile photo is not required
    @DocumentId
    var userDeviceId = "" //Not yet implemented
    var notificationPreference: Boolean = true //True is "notifications on", which is default

}
package com.example.campsiteapp

import com.google.firebase.firestore.DocumentId

open class User () {
    var userName: String = ""
    var userEmail: String = ""
    var userPhoneNumber: String = ""
    var userProfileImage: String = ""
    @DocumentId
    var userDeviceId = ""
    var notificationPreference: Boolean = true //True is "notifications on", which is default

}
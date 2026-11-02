package com.example.campsiteapp

import com.google.firebase.firestore.DocumentId

/**
 * Represents a user’s profile
 *
 * Stores user’s personal information and notification preferences
 * Profile info can be retrieved and displayed in various formats or contexts
 *
 * @property userName the name chosen by the user
 * @property userEmail the email address entered by the user
 * @property userPhoneNumber the phone number entered by the user
 * @property userProfileImage the profile image selected by the user, optional
 * @property userDeviceId the user’s unique device identifier, also serves as the document id in the database
 * @property notificationPreference the user’s notification preference. Opt in = true (default), Opt out = false
 */
open class User {
    var userName: String = ""
    var userEmail: String = ""
    var userPhoneNumber: String = ""
    var userProfileImage: String = ""
    @DocumentId
    var userDeviceId = ""
    var notificationPreference: Boolean = true //True is "notifications on", which is default
    //Currently this is a boolean applying to all notifications, but it could be changed to be a bitfield or something if need be

}
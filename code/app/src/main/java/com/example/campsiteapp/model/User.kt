package com.example.campsiteapp.model

import com.google.firebase.firestore.DocumentId

/**
 * Represents a user’s profile
 *
 * Stores user’s personal information and notification preferences
 * Profile info can be retrieved and displayed in various formats or contexts
 *
 * @property name the name chosen by the user
 * @property email the email address entered by the user
 * @property phone the phone number entered by the user
 * @property profileImage the profile image selected by the user, optional
 * @property deviceId the user’s unique device identifier, also serves as the document id in the database
 * @property notificationsEnabled the user’s notification preference. Opt in = true (default), Opt out = false
 * @property roles the roles which currently apply to the user
 */
open class User {
    var name: String = ""
    var email: String = ""
    var phone: String = ""
    var profileImage: String = ""
    @DocumentId
    var deviceId = ""
    var roles = mutableListOf<String>()
    var notificationsEnabled: Boolean = true //True is "notifications on", which is default
    //Currently this is a boolean applying to all notifications, but it could be changed to be a bitfield or something if need be
}
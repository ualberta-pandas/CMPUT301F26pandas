/*
 * A user profile, stored in the Firestore "profiles" collection.
 *
 * Outstanding issues:
 * - Updating the profile and notification preference is not implemented yet.
 */
package com.example.campsiteapp.model

/**
 * A user of the app, identified by their device ID. Every property has a default
 * so Firestore can create it from a document.
 *
 * @property deviceId ID of the user's device; also the profile's document ID.
 * @property roles roles the user holds, such as "Entrant", "Owner" or "Admin".
 */
data class User(
    val deviceId: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val profileImage: String = "",
    val notificationsEnabled: Boolean = true,
    val roles: List<String> = emptyList(),
)

/*
 * Admin actions on profiles: browsing profiles and removing users who violate
 * policy, along with their campsites.
 *
 * Outstanding issues:
 * - Reads the "profiles" collection directly until a ProfileDatabase exists.
 * - Campsites are matched to owners by device ID, but the current campsite form
 *   stores a typed owner name.
 */
package com.example.campsiteapp.controller

import androidx.compose.runtime.mutableStateListOf
import com.example.campsiteapp.model.Campsite
import com.example.campsiteapp.data.CampsitesDatabase
import com.example.campsiteapp.model.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

/**
 * Lets an admin browse profiles and remove them.
 *
 * @param campsitesDatabase used to remove the campsites of removed owners.
 */
class ProfileAdminController(private val campsitesDatabase: CampsitesDatabase) {
    private val profilesRef = Firebase.firestore.collection("profiles")
    private val _profiles = mutableStateListOf<User>()

    /** All profiles. Updates when the collection changes. */
    val profiles: List<User>
        get() = _profiles

    init {
        profilesRef.addSnapshotListener { snapshots, error ->
            if (error != null) return@addSnapshotListener
            _profiles.clear()
            snapshots?.documents?.forEach { document ->
                document.toObject(User::class.java)?.let { _profiles.add(it) }
            }
        }
    }

    /**
     * Returns the campsites owned by the user with [deviceId].
     */
    fun campsitesOwnedBy(deviceId: String): List<Campsite> =
        campsitesDatabase.campsites.filter { it.owner == deviceId }

    /**
     * Removes the profile with [deviceId] and every campsite they own.
     */
    fun removeProfile(deviceId: String) {
        campsitesOwnedBy(deviceId).forEach { campsitesDatabase.deleteCampsite(it.id) }
        profilesRef.document(deviceId).delete()
    }
}

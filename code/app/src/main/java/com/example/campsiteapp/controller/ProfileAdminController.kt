/*
 * Admin actions on profiles: browsing profiles and removing users who violate
 * policy, along with their campsites.
 *
 * Outstanding issues:
 * - Reads the "profiles" collection directly since ProfileDatabase does not
 *   expose its profiles.
 * - Campsites are matched to owners by device ID, but the current campsite form
 *   stores a typed owner name.
 * - Lotteries and waitlist entries of a removed user are not removed.
 */
package com.example.campsiteapp.controller

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.example.campsiteapp.model.Campsite
import com.example.campsiteapp.model.CampsitesDatabase
import com.example.campsiteapp.model.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

/**
 * Lets an admin browse profiles and remove them.
 *
 * @param campsitesDatabase used to find the campsites of removed owners.
 */
class ProfileAdminController(private val campsitesDatabase: CampsitesDatabase) {
    private val db = Firebase.firestore
    private val profilesRef = db.collection("profiles")
    private val campsitesRef = db.collection("campsites")
    private val _profiles = mutableStateListOf<User>()

    /** All profiles. Updates when the collection changes. */
    val profiles: List<User>
        get() = _profiles

    init {
        profilesRef.addSnapshotListener { snapshots, error ->
            if (error != null) {
                Log.w(TAG, "Could not load profiles", error)
                return@addSnapshotListener
            }
            _profiles.clear()
            snapshots?.documents?.forEach { document ->
                // Skip documents that don't match User instead of crashing the screen.
                runCatching { document.toObject(User::class.java) }
                    .onFailure { Log.w(TAG, "Skipping malformed profile ${document.id}", it) }
                    .getOrNull()
                    ?.let { _profiles.add(it) }
            }
        }
    }

    /**
     * Returns the campsites owned by the user with [deviceId].
     */
    fun campsitesOwnedBy(deviceId: String): List<Campsite> =
        campsitesDatabase.campsites.filter { it.owner == deviceId }

    /**
     * Removes the profile with [deviceId] and every campsite they own. Either
     * everything is removed or nothing is.
     *
     * @param onFailure called if the removal fails.
     */
    fun removeProfile(deviceId: String, onFailure: (Exception) -> Unit) {
        val campsites = campsitesOwnedBy(deviceId)
        db.runBatch { batch ->
            batch.delete(profilesRef.document(deviceId))
            campsites.forEach { batch.delete(campsitesRef.document(it.id)) }
        }.addOnFailureListener(onFailure)
    }

    private companion object {
        const val TAG = "ProfileAdminController"
    }
}

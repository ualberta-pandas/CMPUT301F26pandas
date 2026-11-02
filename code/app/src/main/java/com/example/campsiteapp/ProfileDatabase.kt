package com.example.campsiteapp

import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase

/**
 * Allows for access to and modification of profile documents in the database
 *
 * Contains addProfile, updateProfile, and deleteProfile functions to modify and write to documents in database
 * Has access to state of database once initialized
 * Reads and state of profile database and allows collaborators to access it
 *
 *
 * @property _profiles a mutableStateListOf profiles (User class objects), updated with values from profilesRef
 * @property profilesRef has the collection of profiles stored in the database, updated via snapshot listener
 */

class ProfileDatabase {
    private val _profiles = mutableStateListOf<User>()

    /**
     * Adds a profile to the database
     *
     * Adds data of profile to a new document in the database
     *
     * @param profile a User object whose data is mapped to a document
     */
    fun addProfile(profile: User){
        profilesRef.document(profile.userDeviceId).set(profile)
    }

    /**
     * Updates a profile in the database
     *
     * Replaces the data in the database document associated with oldProfile with the data of newProfile
     *
     * @param oldProfile a User object whose data is stored in a document, to be replaced
     * @param newProfile a User object whose data will be stored in a document, replacing the data of oldProfile
     */
    fun updateProfile(oldProfile: User, newProfile: User){
        profilesRef.document(oldProfile.userDeviceId).set(newProfile)
    }

    /**
     * Removes a profile from the database
     *
     * Finds document with data of profile in the database and deletes it
     *
     * @param profile a User object whose data is stored in a document, to be deleted from the database
     */
    fun deleteProfile(profile: User){
        profilesRef.document(profile.userDeviceId).delete()
    }

    private val profilesRef = Firebase.firestore.collection("profiles")

    init {
        profilesRef.addSnapshotListener { snapshots, error ->
            if (error != null){
                return@addSnapshotListener
            }

            _profiles.clear()

            snapshots?.documents?.forEach { document ->

                val profile = document.toObject(User::class.java)
                if(profile != null){
                    _profiles.add(profile)
                }
            }
        }
    }
}
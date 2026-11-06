/*
Monitor and/or read/write profile documents from database
Outstanding issues:
- lacks a proper listener method, so dynamic updates in compose are not automatically triggered (only an issue if viewing others' profiles)
 */
package com.example.campsiteapp.controller

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.example.campsiteapp.model.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

/**
 * Allows for access to and modification of profile documents in the database
 *
 * Contains [addProfile], [updateProfile], and [deleteProfile] functions to modify and write to documents in database
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
        profilesRef.document(profile.deviceId).set(profile)
    }

    /**
     * Updates a profile in the database
     *
     * Replaces the data in the database document associated with [userId] with the data of [newProfile]
     *
     * @param deviceId the id of the User to be replaced
     * @param newProfile a User object whose data will be stored in a document, replacing the data of oldProfile
     */
    fun updateProfile(deviceId: String, newProfile: User){
        profilesRef.document(deviceId).set(newProfile)
    }

    /**
     * Removes a profile from the database
     *
     * Finds document with data of profile in the database and deletes it
     *
     * @param profile a User object whose data is stored in a document, to be deleted from the database
     */
    fun deleteProfile(profile: User){
        profilesRef.document(profile.deviceId).delete()
    }

    /**
     * Returns a User if data is found at specified document id
     *
     * Looks for a document in database with specified document id
     * If it finds a document, it converts it to a User object and returns it
     * If it doesn’t find the document or an error occurs, it returns null
     *
     * @param documentId the requested document’s id
     * @return User if successful, null if not
     */
    suspend fun getProfileDocument(documentId: String): User? {

        val documentReference = profilesRef.document("profiles")
        return try {
            val snapshot = documentReference.get().await()
            if (snapshot.exists()) {
                val user = snapshot.toObject(User::class.java)
                user
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("getProfileDocument", "Error fetching document: ${e.localizedMessage}", e)
            null
        }
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
package com.example.campsiteapp

import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase

class ProfileDatabase {
    private val _profiles = mutableStateListOf<User>()

    val profiles: List<User>
        get() = _profiles

    fun addProfile(profile: User){
        profilesRef.document(profile.userDeviceId).set(profile)
    }
    fun updateProfile(oldProfile: User, newProfile: User){
        profilesRef.document(oldProfile.userDeviceId).set(newProfile)
    }
    fun deleteProfile(profile: User){
        profilesRef.document(profile.userDeviceId).delete()
    }

    private val db = Firebase.firestore

    private val profilesRef = db.collection("profiles")

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
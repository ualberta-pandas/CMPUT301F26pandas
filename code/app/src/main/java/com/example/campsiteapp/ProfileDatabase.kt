package com.example.campsiteapp

import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase

class ProfileDatabase {
    private val _profiles = mutableStateListOf<User>()

    fun addProfile(profile: User){
        profilesRef.document(profile.userDeviceId).set(profile)
    }
    fun updateProfile(oldProfile: User, newProfile: User){
        profilesRef.document(oldProfile.userDeviceId).set(newProfile)
    }
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
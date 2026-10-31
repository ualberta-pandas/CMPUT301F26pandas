package com.example.campsiteapp

import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore



class CampsitesDatabase {

    private val db = Firebase.firestore

    private val campsitesRef = db.collection("campsites")

    private val _campsites = mutableStateListOf<CampsiteObject>()

    init {
        campsitesRef.addSnapshotListener { snapshots, error ->
            if (error != null){
                return@addSnapshotListener
            }

            _campsites.clear()

            snapshots?.documents?.forEach { document ->

                val campsite = document.toObject(CampsiteObject::class.java)
                if(campsite != null){
                    _campsites.add(
                        campsite.copy(id = document.id)

                    )
                }
            }
        }
    }

    val campsites: List<CampsiteObject>
        get() = _campsites

    fun addCampsite(campsite: CampsiteObject){
        campsitesRef.add(
            mapOf(
                "name" to campsite.name,
                "location" to campsite.location,
                "occupancy" to campsite.occupancy,
                "owner" to campsite.owner,
                "mapLocation" to campsite.mapLocation,
                "adates" to campsite.adates,
                "description" to campsite.description,
                "tags" to campsite.tags,
                "rules" to campsite.rules,
                "image" to campsite.image
            )

        )
    }



}
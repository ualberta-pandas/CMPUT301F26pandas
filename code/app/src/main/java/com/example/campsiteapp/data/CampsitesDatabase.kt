package com.example.campsiteapp.data

import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore



class CampsitesDatabase {

    private val db = Firebase.firestore

    private val campsitesRef = db.collection("campsites")

    private val _campsites = mutableStateListOf<Campsite>()

    init {
        campsitesRef.addSnapshotListener { snapshots, error ->
            if (error != null){
                return@addSnapshotListener
            }

            _campsites.clear()

            snapshots?.documents?.forEach { document ->

                val campsite = document.toObject(Campsite::class.java)
                if(campsite != null){
                    _campsites.add(
                        campsite.copy(id = document.id)

                    )
                }
            }
        }
    }

    val campsites: List<Campsite>
        get() = _campsites

    fun addCampsite(campsite: Campsite){
        campsitesRef.add(
            mapOf(
                "name" to campsite.name,
                "location" to campsite.location,
                "occupancy" to campsite.occupancy,
                "owner" to campsite.owner,
                "mapLocation" to campsite.mapLocation,
                "startDate" to campsite.startDate,
                "endDate" to campsite.startDate,
                "description" to campsite.description,
                "tags" to campsite.tags,
                "rules" to campsite.rules,
                "image" to campsite.image
            )

        )
    }



}
package com.example.campsiteapp

import androidx.compose.runtime.mutableStateListOf
import com.example.campsiteapp.data.Campsite
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class CampsiteDatabase {
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
                "maxOccupancy" to campsite.maxOccupancy,

                "owner" to campsite.owner,

                "startDate" to campsite.startDate.toString(),
                "endDate" to campsite.endDate.toString(),

                "description" to campsite.description,
            )
        )
    }

}
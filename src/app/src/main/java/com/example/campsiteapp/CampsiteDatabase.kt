package com.example.campsiteapp

import androidx.compose.runtime.mutableStateListOf
import com.example.campsiteapp.data.Campsite
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.time.LocalDate

object CampsiteDatabase {
    private val db = Firebase.firestore
    private val campsitesRef = db.collection("entrant_campsites")
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

    fun seedDummyData() {
        repeat(5) { i ->
            addCampsite(
                Campsite(
                    id = "$i",
                    name = "Test Site $i",
                    location = "Edmdeonton, AB",
                    maxOccupancy = 4 + i,
                    owner = "test_device",
                    description = "Dummy row $i",
                    rating = 5.5,
                    startDate = LocalDate.of(2026, 9, 22),
                    endDate = LocalDate.of(2026, 10, 30)
                )
            )
        }
    }

    fun getCampsiteById(id: String): Campsite? {
        for (campsite in _campsites) {
            if (campsite.id == id) {
                return campsite
            }
        }
        return null
    }
}
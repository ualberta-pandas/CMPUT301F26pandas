package com.example.campsiteapp.data

import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

/**

    * This is the database, and it stores and retrieves data from Firestore database


    * @property db stores the database
 * @property campsitesRef stores the campsite collection for future use
 * @property _campsites is an empty mutable list to store campsites
 * @property campsites converts _campsites mutable into normal List

 */

class CampsitesDatabase {

    private val db = Firebase.firestore

    private val campsitesRef = db.collection("campsites")

    private val _campsites = mutableStateListOf<Campsite>()

    init {


        /**
         * This init is used to retrieve the data in the campsite collection
         * to store it in _campsites mutable list
         * by looping through all documents in the collection
         */

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

        /**
         * this function is used by our controller to add campsites into our database
         * through campsitesRef.
         * It maps different collection fields to their
         * corresponding campsite properties
         */

        campsitesRef.add(
            mapOf(
                "name" to campsite.name,
                "location" to campsite.location,
                "occupancy" to campsite.occupancy,
                "owner" to campsite.owner,
                "latitude" to campsite.latitude,
                "longitude" to campsite.longitude,
                "startDate" to campsite.startDate,
                "endDate" to campsite.endDate,
                "description" to campsite.description,
                "tags" to campsite.tags,
                "rules" to campsite.rules,
                "image" to campsite.image,
                "price" to campsite.price,
                "status" to campsite.status
            )

        )
    }

    //edit campsites

    fun updateCampsite(oldCampsiteDetails: Campsite, newCampsiteDetails: Campsite){

        /**
         * This function is used by our controller to update campsite details.
         * It retrieves the old campsite index from the _campsites list, then
         * it replaces it with the new campsite
         *
         *
         * @param oldCampsiteDetails holds old campsite details passed by the controller
         * @param newCampsiteDetails holds new campsite details passed by the controller

         */

        // The edit form doesn't set an id, so keep the old one.
        val updatedCampsite = newCampsiteDetails.copy(id = oldCampsiteDetails.id)

        val index = _campsites.indexOf(oldCampsiteDetails)

        if(index != -1){
            _campsites[index] = updatedCampsite
        }

        campsitesRef.document(updatedCampsite.id).set(
            mapOf(
                "name" to updatedCampsite.name,
                "location" to updatedCampsite.location,
                "occupancy" to updatedCampsite.occupancy,
                "owner" to updatedCampsite.owner,
                "latitude" to updatedCampsite.latitude,
                "longitude" to updatedCampsite.longitude,
                "startDate" to updatedCampsite.startDate,
                "endDate" to updatedCampsite.endDate,
                "description" to updatedCampsite.description,
                "tags" to updatedCampsite.tags,
                "rules" to updatedCampsite.rules,
                "image" to updatedCampsite.image,
                "price" to updatedCampsite.price,
                "status" to updatedCampsite.status
            )
        )

    }



    /** Deletes the campsite with [id]. */
    fun deleteCampsite(id: String) {
        campsitesRef.document(id).delete()
    }
}
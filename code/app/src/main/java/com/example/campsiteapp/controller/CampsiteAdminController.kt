/*
 * Admin actions on campsites: browsing and removing listings.
 */
package com.example.campsiteapp.controller

import com.example.campsiteapp.model.Campsite
import com.example.campsiteapp.data.CampsitesDatabase

/**
 * Lets an admin browse and remove campsites.
 *
 * @param campsitesDatabase where campsites are stored.
 */
class CampsiteAdminController(private val campsitesDatabase: CampsitesDatabase) {
    /** All campsites. Updates when the database changes. */
    val campsites: List<Campsite>
        get() = campsitesDatabase.campsites

    /**
     * Removes the campsite with [id].
     */
    fun removeCampsite(id: String) {
        campsitesDatabase.deleteCampsite(id)
    }
}

/*
 * Admin actions on uploaded images: browsing the image log and removing images
 * that violate policy.
 *
 * Outstanding issues:
 * - Reads the "imageLog" collection directly since ImageLog does not expose its
 *   entries or their document ids.
 * - Removed images stay on Cloudinary, since deleting them needs the API secret.
 * - Copies of a campsite stored in lotteries keep the removed image.
 * - Images uploaded before the image log existed are not listed.
 */
package com.example.campsiteapp.controller

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore

/**
 * An image in the image log.
 *
 * @property id id of its image log document.
 * @property url where the image is hosted.
 * @property uploaderId device ID of the user who uploaded it.
 */
data class LoggedImage(val id: String, val url: String, val uploaderId: String)

/**
 * Lets an admin browse uploaded images and remove them.
 */
class ImageAdminController {
    private val db = Firebase.firestore
    private val imageLogRef = db.collection("imageLog")
    private val _images = mutableStateListOf<LoggedImage>()

    /** All logged images. Updates when the image log changes. */
    val images: List<LoggedImage>
        get() = _images

    init {
        imageLogRef.addSnapshotListener { snapshots, error ->
            if (error != null) {
                Log.w(TAG, "Could not load images", error)
                return@addSnapshotListener
            }
            _images.clear()
            snapshots?.documents?.forEach { document ->
                val url = document.get("image") as? String
                if (url.isNullOrBlank()) return@forEach
                _images.add(LoggedImage(document.id, url, document.get("deviceId") as? String ?: ""))
            }
        }
    }

    /**
     * Removes [image] from the image log and from every campsite and profile
     * showing it. Either everything is removed or nothing is.
     *
     * @param onFailure called if the removal fails.
     */
    fun removeImage(image: LoggedImage, onFailure: (Exception) -> Unit) {
        val campsites = db.collection("campsites").whereEqualTo("image", image.url).get()
        val profiles = db.collection("profiles").whereEqualTo("profileImage", image.url).get()
        Tasks.whenAllSuccess<QuerySnapshot>(campsites, profiles)
            .onSuccessTask { (campsiteDocuments, profileDocuments) ->
                db.runBatch { batch ->
                    batch.delete(imageLogRef.document(image.id))
                    campsiteDocuments.forEach { batch.update(it.reference, "image", "") }
                    profileDocuments.forEach { batch.update(it.reference, "profileImage", "") }
                }
            }
            .addOnFailureListener(onFailure)
    }

    private companion object {
        const val TAG = "ImageAdminController"
    }
}

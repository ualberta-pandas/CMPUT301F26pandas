/*
Image log entries to be viewed by admin
*/
package com.example.campsiteapp.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

/**
 * An entry in the image log
 *
 * Stores an Image and associated uploader (by deviceId)
 *
 * @property deviceId the user uploading the image
 * @property image the image to be logged
 */
class ImageLogEntry {
    var image = ""
    var deviceId = ""
    //If wanting to sort by time uploaded, add functionality in ImageLog
    @ServerTimestamp
    val timestamp: Timestamp? = null
}
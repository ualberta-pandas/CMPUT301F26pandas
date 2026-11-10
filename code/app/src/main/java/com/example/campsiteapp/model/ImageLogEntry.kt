/*
Image log entries to be viewed by admin
*/
package com.example.campsiteapp.model

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
}
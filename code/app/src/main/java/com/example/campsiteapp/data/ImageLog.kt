/*
Monitor and/or read/write log entry documents from database
Outstanding issues:
- lacks a proper listener method, so dynamic updates in compose are not automatically triggered (potential issue when viewing logs as admin)
 */
package com.example.campsiteapp.data

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.example.campsiteapp.model.ImageLogEntry
import com.example.campsiteapp.model.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

/**
 * Allows for access to and modification of logged images
 *
 * Contains [addImageLogEntry] and [deleteImageLogEntry] functions to modify and write to documents in database
 * Has access to state of database once initialized
 * Reads and state of profile database and allows collaborators to access it
 *
 *
 * @property _imageLogEntries a mutableStateListOf log entries (ImageLogEntry class objects), updated with values from imageLogRef
 * @property imageLogRef has the collection of log entries stored in the database, updated via snapshot listener
 */
class ImageLog {
    private val _imageLogEntries = mutableStateListOf<ImageLogEntry>()
    private val imageLogRef = Firebase.firestore.collection("imageLog")

    /**
     * Adds an image log entry to the database
     *
     * Adds data of log entry to a new document in the database
     *
     * @param imageLogEntry an ImageLogEntry object whose data is mapped to a document
     */
    fun addImageLogEntry(imageLogEntry: ImageLogEntry){
        imageLogRef.add(imageLogEntry)
            .addOnSuccessListener { docRef ->
                val autoId = docRef.id
            }
    }


    /**
     * Removes an image log entry from the database
     *
     * Finds document with data of log entry in the database and deletes it
     *
     * @param documentId id of an ImageLogEntry object whose data is stored in a document, to be deleted from the database
     */
    fun deleteImageLogEntry(documentId: String){
        if(documentId!=""){
            imageLogRef.document(documentId).delete()
        }
    }

    init {
        imageLogRef.addSnapshotListener { snapshots, error ->
            if (error != null){
                return@addSnapshotListener
            }

            _imageLogEntries.clear()

            snapshots?.documents?.forEach { document ->

                val imageLogEntry = document.toObject(ImageLogEntry::class.java)
                if(imageLogEntry != null){
                    _imageLogEntries.add(imageLogEntry)
                }
            }
        }
    }
}
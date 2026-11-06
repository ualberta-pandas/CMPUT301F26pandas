package com.example.campsiteapp

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import com.example.campsiteapp.model.User

/**
 * Returns a User if data is found at specified document id
 *
 * Looks for a document in database with specified document id
 * If it finds a document, it converts it to a User object and returns it
 * If it doesn’t find the document or an error occurs, it returns null
 *
 * @param documentId the requested document’s id
 * @return User if successful, null if not
 */
suspend fun getProfileDocument(documentId: String): User? {
    val db = Firebase.firestore
    val documentReference = db.collection("profiles").document(documentId)
    return try {
        val snapshot = documentReference.get().await()
        if (snapshot.exists()) {
            val user = snapshot.toObject(User::class.java)
            user
        } else {
            null
        }
    } catch (e: Exception) {
        Log.e("getProfileDocument", "Error fetching document: ${e.localizedMessage}", e)
        null
    }
}
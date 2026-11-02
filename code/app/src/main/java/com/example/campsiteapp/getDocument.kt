package com.example.campsiteapp

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

suspend fun getDocument(collectionPath: String, documentId: String): User? {
    val db = Firebase.firestore
    val documentReference = db.collection(collectionPath).document(documentId)
    return try {
        val snapshot = documentReference.get().await()
        if (snapshot.exists()) {
            val user = snapshot.toObject(User::class.java)
            user
        } else {
            null
        }
    } catch (e: Exception) {
        Log.e("getDocument", "Error fetching document: ${e.localizedMessage}", e)
        null
    }
}

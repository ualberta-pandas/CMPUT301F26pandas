package com.example.campsiteapp.controller

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore


fun saveRating(campsiteId: String, deviceId: String, newStars: Int) {
    val db = Firebase.firestore
    val campsiteRef = db.collection("campsites").document(campsiteId)
    val ratingRef = campsiteRef.collection("ratings").document(deviceId)

    ratingRef.get().addOnSuccessListener { old ->
        val oldStars = old.getLong("stars")
        val change = if (oldStars != null) newStars - oldStars else newStars.toLong()
        val newCount = if (oldStars == null) 1L else 0L

        val batch = db.batch()
        batch.set(
            ratingRef,
            mapOf("stars" to newStars, "createdAt" to FieldValue.serverTimestamp())
        )
        batch.set(
            campsiteRef,
            mapOf(
                "ratingSum" to FieldValue.increment(change),
                "ratingCount" to FieldValue.increment(newCount)
            ),
            SetOptions.merge()
        )
        batch.commit()
    }
}
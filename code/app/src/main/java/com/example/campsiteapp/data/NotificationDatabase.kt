package com.example.campsiteapp.data

import androidx.compose.runtime.mutableStateListOf
import com.example.campsiteapp.model.Notification
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class NotificationDatabase {

    private val db = Firebase.firestore

    private val notificationsRef = db.collection("notifications")

    private val _notifications = mutableStateListOf<Notification>()

    init {
        notificationsRef.addSnapshotListener { snapshots, exception ->
            if (exception != null){
                return@addSnapshotListener
            }

            _notifications.clear()

            snapshots?.documents?.forEach { document ->
                val notification = document.toObject(Notification::class.java)

                if (notification != null){
                    _notifications.add(
                        notification.copy(id =document.id)
                    )
                }
            }
        }
    }

    val notifications: List<Notification>
            get() = _notifications

}
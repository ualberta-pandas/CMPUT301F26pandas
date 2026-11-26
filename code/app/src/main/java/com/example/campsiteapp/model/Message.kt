package com.example.campsiteapp.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class Message(
    @DocumentId
    var messageId: String = "",
    var messageText: String = "",
    var senderId: String = "",
    var senderName: String = "",
    @ServerTimestamp
    val timestamp: Timestamp? = null
)

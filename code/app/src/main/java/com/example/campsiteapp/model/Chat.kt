package com.example.campsiteapp.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ServerTimestamp

class Chat(
    @DocumentId
    var chatId: String = "",
    var userIds: List<String> = emptyList(),
    var userNames: List<String> = emptyList(),
    var lastMessageText: String = "",
    var lastMessageSenderId: String = "",
    var userProfileImages: List<String> = emptyList(),
    @ServerTimestamp
    var lastMessageTimestamp: Timestamp? = null
){
    fun getOtherUserId(userId:String): String{
        val ids = this.userIds
        return if(ids.size==2){
            if(ids[0]==userId){
                ids[1]
            } else {
                ids[0]
            }
        } else {
            userId
        }
    }
}

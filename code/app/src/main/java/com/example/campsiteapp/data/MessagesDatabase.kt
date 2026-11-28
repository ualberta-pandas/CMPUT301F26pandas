/*
 * Manage data in chats and messages collections
 *
 * Outstanding issues:
 * - Function deleteMessages in deleteUserChats will likely raise an exception if there are over 500 messages (according to documentation)
 */
package com.example.campsiteapp.data

import android.util.Log
import com.example.campsiteapp.model.Chat
import com.example.campsiteapp.model.Message
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue.serverTimestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlin.String
import kotlin.collections.List

/**
 * Access messages and chats in database
 *
 * Access, create, and modify chat documents and their nested message documents
 */
class MessagesDatabase {
    /**
     * Get updated chats for a user
     *
     * Get new, updated chats list when document changes
     *
     * @param userId the id of the user
     * @return a Flow of Chat object list instances
     */
    fun chatsListener(userId: String): Flow<List<Chat>> = callbackFlow {
        val listener = chatsRef
            .whereArrayContains("userIds", userId)
            .orderBy("lastMessageTimestamp",Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
                if(exception!=null){
                    close(exception)
                    return@addSnapshotListener
                }
                if(snapshot!=null){
                    val chats = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(
                            Chat::class.java,
                            DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                        )
                    }
                    trySend(chats)
                }
            }
        awaitClose {
            listener.remove()
        }
    }

    /**
     * Get updated chat messages
     *
     * Get new, updated chat messages list when document changes
     *
     * @param chatId the id of a particular chat
     * @return a Flow of Message object list instances
     */
    fun messagesListener(chatId: String): Flow<List<Message>> = callbackFlow {
        val listener = chatsRef
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, exception ->
                if(exception!=null){
                    close(exception)
                    return@addSnapshotListener
                }
                if(snapshot!=null){
                    val messages = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(
                            Message::class.java,
                            DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                        )
                    }
                    trySend(messages)
                }
            }
        awaitClose {
            listener.remove()
        }
    }

    /**
     * Get updated chat
     *
     * Get new, updated chat object when document changes
     *
     * @param chatId the id of a particular chat
     * @return a Flow of Chat object instances
     */
    fun chatUpdateListener(chatId: String): Flow<Chat> = callbackFlow {
        val listener = chatsRef
            .document(chatId)
            .addSnapshotListener { snapshot, exception ->
                if(exception!=null){
                    close(exception)
                    return@addSnapshotListener
                }
                if(snapshot!=null){
                    var updatedChat = snapshot.toObject(Chat::class.java)
                    if (updatedChat==null){
                        updatedChat=Chat()
                    }
                    trySend(updatedChat)
                }
            }
        awaitClose {
            listener.remove()
        }
    }

    /**
     * Adds a chat to the database
     *
     * Adds data of chat to a new document in the database and returns new object
     * or updates and returns the chat if it already exists
     *
     * @param userIds the device ids of the users in the chat
     * @param profileImages the profile images of the users in the chat
     * @param names the names of the users in the chat
     */
    suspend fun openChat(userIds: List<String>, profileImages: List<String>, names: List<String>): Chat {
        val chat = Chat()
        chat.userIds = userIds
        val getChat = getChat(chat)
        if (getChat==null){
            chat.userProfileImages = profileImages
            chat.userNames = names
            val newChat = chatsRef
                .add(
                    chat
                )
                .await()
            Log.d("debug", "chat created: id: ${getChat(chat)?.chatId}")
            chat.chatId=newChat.id
            return chat
        } else {
            Log.d("debug", "chat accessed: id: ${getChat.chatId}")
            val updateRef = chatsRef.document(getChat.chatId)
            //keeping timestamp consistent across chat and message without having to update chat with new message timestamp
            updateRef
                .update(
                    mapOf(
                        "userIds" to userIds,
                        "userProfileImages" to profileImages,
                        "userNames" to names
                    )
                )
                .await()

            val updatedChat = updateRef.get().await().toObject(Chat::class.java)
            //If it is null, the chat the messaging system is working with can not update properly
            return updatedChat ?: chat
        }
    }



    /**
     * Updates a chat in the database
     *
     * Adds a new message to a chat if it exists
     *
     * @param chat a Chat object whose data is stored in a document
     * @param message a Message object whose data will be stored in the document associated with chat
     */
    suspend fun addMessageToChat(chat: Chat, message: Message){
        val chatRef = chatsRef.document(chat.chatId)

        Firebase.firestore.batch().apply{
            val newMessageRef = chatRef.collection("messages").document()
            //keeping timestamp consistent across chat and message without having to update chat with new message timestamp
            set(newMessageRef, message)
            update(
                chatRef,
                mapOf(
                    "lastMessageText" to message.messageText,
                    "lastMessageSenderId" to message.senderId,
                    "lastMessageTimestamp" to serverTimestamp()
                )
            )
        }.commit().await()
    }

    /**
     * Gets a particular chat from the database
     *
     * Finds documents with first userId
     * Filter to get document with second userId from those results
     * Returns chat if found, or null
     *
     * @param chat the chat to check database for
     */
    suspend fun getChat(chat: Chat):Chat?{
        try{
            val snapshot = chatsRef
                .whereArrayContains("userIds",chat.userIds[0])
                .get()
                .await()
            var resultingChats=snapshot.toObjects(Chat::class.java)
            if(resultingChats.isNotEmpty()) {
                resultingChats = resultingChats.filter { it.userIds.contains(chat.userIds[1]) }
                return resultingChats.firstOrNull()
            } else {
                return null
            }
        } catch (e:Exception){
            Log.e("debug", "Could not get chat associated with two users. Make sure that's due to intentional logic. Error: ${e.localizedMessage}",e)
            return null
        }


    }



    /**
     * Removes a user's chats from the database
     *
     * Finds documents with user's userId in chats using [getUserChats]
     * Deletes all associated messages with [deleteMessages]
     *
     * @param userId the id of the user whose chats are to be deleted
     */
    suspend fun deleteUserChats(userId: String){
        val userChats=getUserChats(userId)
        for(chat in userChats){
            if(!deleteMessages(chat)){
                return
            }
            chatsRef.document(chat.chatId).delete()
        }
    }

    /**
     * Retrieves a particular user's chats from the database
     *
     * Finds documents with user's userId in chats
     *
     * @param userId the id of the user whose chats to retrieve
     * @return a list of the user's chats, or, if none exist, an empty list
     */
    suspend fun getUserChats (userId: String): List<Chat>{
        return try{
            val snapshot = chatsRef
                .whereArrayContains("userIds", userId)
                .get()
                .await()
            snapshot.toObjects(Chat::class.java)
        } catch (e: Exception){
            Log.e("getUserChats", "Could not get user's chats: ${e.localizedMessage}", e)
            emptyList()
        }
    }

    /**
     * Removes all messages in a chat
     *
     * Deletes all messages in chat subcollection
     *
     * @param chat the chat whose messages are to be deleted
     */
    suspend fun deleteMessages(chat: Chat): Boolean{
        val messagesRef = chatsRef
            .document(chat.chatId)
            .collection("messages")
        try{
            val snapshot = messagesRef.get().await()

            if(snapshot.isEmpty){
                return true
            }

            val batch = Firebase.firestore.batch()

            for (message in snapshot.documents){
                batch.delete(message.reference)
            }

            batch.commit().await()
            return true

        } catch (e: Exception){
            Log.e("deleteMessages", "Error deleting documents: ${e.localizedMessage}", e)
            return false
        }
    }

    private val chatsRef = Firebase.firestore.collection("chats")
}
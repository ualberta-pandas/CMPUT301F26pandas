package com.example.campsiteapp.data

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import com.google.firebase.firestore.FieldValue

/**
 * Device id class, device id has been implemented in MainActivity.kt
 * To use, pass into deviceIds variable into screens or methods
 *
 * This class has methods fetch, register and clear.
 * as well as save, get and delete user info methods for ease of use:)
 *
 * AI disclosure: Claude recommended SHA-256 hash and set up line 23-32
 **/
class DeviceIDs(context: Context) {

    private val docs = FirebaseFirestore.getInstance().collection("device_ids")

    @SuppressLint("HardwareIds")
    private val hash: String = MessageDigest.getInstance("SHA-256")
        .digest(
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                .toByteArray()
        )
        .joinToString("") { "%02x".format(it) }

    // Returns the hash if this device is in the database, otherwise null
    suspend fun fetchDeviceId(): String? =
        if (docs.document(hash).get().await().exists()) hash else null

    // Adds this device to the database and returns its hash
    suspend fun registerDeviceId(): String {
        docs.document(hash).set(emptyMap<String, Any>(), SetOptions.merge()).await()
        return hash
    }

    // Removes this device (and its user info) from the database
    suspend fun clearDeviceId() {
        docs.document(hash).delete().await()
    }

    // Saves user info; keys you pass overwrite existing ones, others are kept
    suspend fun saveUserInfo(info: Map<String, Any>) {
        docs.document(hash).set(mapOf("userInfo" to info), SetOptions.merge()).await()
    }

    // Returns saved user info, or null if there is none
    @Suppress("UNCHECKED_CAST")
    suspend fun getUserInfo(): Map<String, Any>? =
        docs.document(hash).get().await().get("userInfo") as? Map<String, Any>

    suspend fun deleteUserInfo() {
        docs.document(hash)
            .update("userInfo", FieldValue.delete())
            .await()
    }

    suspend fun deleteUserInfoField(field: String) {
        docs.document(hash)
            .update("userInfo.$field", FieldValue.delete())
            .await()
    }
}
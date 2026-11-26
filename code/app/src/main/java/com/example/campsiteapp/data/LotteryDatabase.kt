package com.example.campsiteapp.data

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.example.campsiteapp.model.Campsite
import com.example.campsiteapp.model.Lottery
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

/**
 * Saves and loads lotteries in the Firestore "lotteries" collection
 *
 *
 * @property lotteriesRef Firestore "lotteries" collection
 * @property lotteries read-only view of lotteries currently in Firestore
 */
class LotteryDatabase : LotteryStorage {
    private val db = Firebase.firestore
    private val lotteriesRef = db.collection("lotteries")
    private val campsitesRef = db.collection("campsites")

    private val _lotteries = mutableStateListOf<Lottery>()
    private val latestCampsites = mutableMapOf<String, Campsite>()
    private var rawLotteries: List<Lottery> = emptyList()

    val lotteries: List<Lottery> get() = _lotteries

    init {
        lotteriesRef.addSnapshotListener { snapshots, error ->
            if (error != null) {
                Log.e("LotteryDatabase", "Lottery listen failed", error)
                return@addSnapshotListener
            }

            rawLotteries = snapshots?.documents?.mapNotNull { document ->
                try {
                    document.toObject(Lottery::class.java)?.copy(id = document.id)
                } catch (e: Exception) {
                    Log.e("LotteryDatabase", "Skipping bad lottery ${document.id}: ${e.message}")
                    null
                }
            } ?: emptyList()

            rebuild()
        }

        campsitesRef.addSnapshotListener { snapshots, error ->
            if (error != null) {
                Log.e("LotteryDatabase", "Campsite listen failed", error)
                return@addSnapshotListener
            }

            snapshots?.documents?.forEach { document ->
                try {
                    document.toObject(Campsite::class.java)
                        ?.copy(id = document.id)
                        ?.let { latestCampsites[document.id] = it }
                } catch (e: Exception) {
                    Log.e("LotteryDatabase", "Skipping bad campsite ${document.id}: ${e.message}")
                }
            }

            rebuild()
        }
    }

    // Puts the newest campsite data into each lottery, then updates the list
    private fun rebuild() {
        val merged = rawLotteries.map { lottery ->
            val fresh = latestCampsites[lottery.campsite.id]
            if (fresh != null) lottery.copy(campsite = fresh) else lottery
        }
        _lotteries.clear()
        _lotteries.addAll(merged)
    }

    /**
     * Saves new lottery to database. Firestore assigns doc id
     *
     * @param lottery Lottery to save; id is ignored
     */
    override fun addLottery(lottery: Lottery) {
        lotteriesRef.add(lottery.copy(id = ""))
    }

    /**
     * Overwrites an existing lottery with new details
     *
     * @param lottery Lottery to update; its id picks which doc is replaced
     */
    override fun updateLottery(lottery: Lottery) {
        if(lottery.id.isEmpty()) {
            return
        }

        lotteriesRef.document(lottery.id).set(lottery.copy(id = ""))
    }
}
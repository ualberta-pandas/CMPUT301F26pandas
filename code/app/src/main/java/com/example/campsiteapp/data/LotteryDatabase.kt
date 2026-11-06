package com.example.campsiteapp.data

import androidx.compose.runtime.mutableStateListOf
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
    private val lotteriesRef = Firebase.firestore.collection("lotteries")
    private val _lotteries = mutableStateListOf<Lottery>()

    val lotteries: List<Lottery> get() = _lotteries

    init {
        lotteriesRef.addSnapshotListener { snapshots, error ->
            if(error != null) {
                return@addSnapshotListener
            }

            _lotteries.clear()
            snapshots?.documents?.forEach { document ->
                document.toObject(Lottery::class.java)?.let { lottery ->
                    _lotteries.add(lottery.copy(id=document.id))
                }
            }
        }
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
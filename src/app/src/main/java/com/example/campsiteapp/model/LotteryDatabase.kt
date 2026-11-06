package com.example.campsiteapp.model

import androidx.compose.runtime.mutableStateListOf
import com.example.campsiteapp.data.Lottery
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class LotteryDatabase {
    private val db = Firebase.firestore
    private val lotteriesRef = db.collection("lotteries")
    private val _lotteries = mutableStateListOf<Lottery>()

    init {
        lotteriesRef.addSnapshotListener { snapshots, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            _lotteries.clear()

            snapshots?.documents?.forEach { document ->
                val lottery = document.toObject(Lottery::class.java)
                if (lottery != null) {
                    _lotteries.add(lottery.copy(id = document.id))
                }
            }
        }
    }

    val lotteries: List<Lottery>
        get() = _lotteries

    fun addLottery(lottery: Lottery) {
        lotteriesRef.add(
            mapOf(
                "campsite" to lottery.campsite,
                "campsiteId" to lottery.campsiteId,
                "numWinners" to lottery.numWinners,
                "lotteryStartDate" to lottery.lotteryStartDate,
                "lotteryEndDate" to lottery.lotteryEndDate,
                "waitlistMax" to lottery.waitlistMax,
                "waitlist" to lottery.waitlist
            )
        )
    }
}
package com.example.campsiteapp.controller

import com.example.campsiteapp.data.LotteryDatabase
import com.example.campsiteapp.model.Lottery
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import java.time.LocalDate

object PoolingSystem {
    val db = Firebase.firestore.collection("lotteries")
    private var lotteryDatabase: LotteryDatabase = LotteryDatabase()

    /**
        Runs a query to find the number of lotteries that have a termination date equal to, or less than the current date.
        Is called whenever a user calls MainActivity. The query is what we want, and the addOnSucessListener calls the query,
        and gives us the results that we can go through
     **/
    fun getTerminatedLotteries(): ArrayList<Lottery>{
        val drawnLotteries: ArrayList<Lottery> = ArrayList()
        val query: Query = db.whereLessThan("lotteryEndDate", LocalDate.now().toString())

        query.get().addOnSuccessListener { result ->
            for (doc in result){
                val lottery = doc.toObject(Lottery::class.java).copy(id = doc.id)
                if (lottery.winners.isNotEmpty() || lottery.waitlist.isEmpty()) continue

                drawnLotteries.add(lottery) // Might help when it comes to debugging
                drawLotteryWinner(lottery)

                lotteryDatabase.updateLottery(lottery)
            }
        }

        return drawnLotteries
    }

    /**
        Randomly selects a winner for the lottery. Only called from the getTerminatedLotteries function,
        when it returns a list of lotteries that are past their due date
    **/
    fun drawLotteryWinner(lottery: Lottery){
        val lotteryWaitlist = lottery.waitlist.filter { it.isNotBlank() }.toMutableList()

        if (lotteryWaitlist.isNotEmpty()) {
            var winnersIncr: Int = 0;
            for (i in 1..lotteryWaitlist.size){
                if (winnersIncr == lottery.numWinners) break;

                val randomDeviceId: String = lotteryWaitlist.random()
                lottery.winners.add(randomDeviceId)
                lotteryWaitlist.remove(randomDeviceId)

                winnersIncr++
            }

        } else {
            for (i in 1..lottery.numWinners){
                lottery.winners.add("NullPerson")
            }
        }
    }
}
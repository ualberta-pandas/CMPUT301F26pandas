package com.example.campsiteapp.data

import com.example.campsiteapp.model.Lottery

//For testing to not interfere with database
interface LotteryStorage {
    fun addLottery(lottery: Lottery)

    fun updateLottery(lottery: Lottery)
}
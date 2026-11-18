package com.example.campsiteapp.controller

import com.example.campsiteapp.model.Lottery
import com.example.campsiteapp.data.LotteryDatabase


object EntrantWaitlistController {
    /**
        Calls for the device_id and adds the entrant to the specific lottery
     **/

    private var lotteryDatabase: LotteryDatabase = LotteryDatabase()

    fun addEntrantToWaitlist(lottery: Lottery, deviceId: String) {
        lottery.waitlist.add(deviceId)
        lotteryDatabase.updateLottery(lottery);
    }

    fun removeEntrantFromWaitlist(lottery: Lottery?, deviceId: String) {
        lottery?.waitlist?.remove(deviceId)
    }

    fun verifyUserOnWaitlist(lottery: Lottery?, deviceId: String): Boolean {
        return lottery?.waitlist?.contains(deviceId) ?: false
    }
}
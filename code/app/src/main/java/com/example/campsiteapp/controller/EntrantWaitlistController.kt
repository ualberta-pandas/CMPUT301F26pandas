package com.example.campsiteapp.controller

import com.example.campsiteapp.model.Lottery

object EntrantWaitlistController {
    /**
        Calls for the device_id and adds the entrant to the specific lottery
     **/

    fun addEntrantToWaitlist(lottery: Lottery?, deviceId: String) {
        lottery?.waitlist?.add(deviceId)
    }

    fun removeEntrantFromWaitlist(lottery: Lottery?, deviceId: String) {
        lottery?.waitlist?.remove(deviceId)
    }

    fun verifyUserOnWaitlist(lottery: Lottery?, deviceId: String): Boolean {
        return lottery?.waitlist?.contains(deviceId) ?: false
    }
}
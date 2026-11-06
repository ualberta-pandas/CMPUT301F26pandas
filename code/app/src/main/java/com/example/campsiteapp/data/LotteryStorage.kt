package com.example.campsiteapp.data

import com.example.campsiteapp.model.Lottery

/**
 * Where lotteries get saved.
 *
 * LotteryDatabase implements this against Firestore. Controllers depend on this
 * interface instead of the database directly, so tests can supply a fake that
 * doesn't need Firebase.
 */
interface LotteryStorage {
    /**
     * Saves new lottery
     *
     * @param lottery Lottery to save; id is ignored
     */
    fun addLottery(lottery: Lottery)

    /**
     * Overwrites an existing lottery with new details
     *
     * @param lottery Lottery to update; its id picks which doc is replaced
     */
    fun updateLottery(lottery: Lottery)
}
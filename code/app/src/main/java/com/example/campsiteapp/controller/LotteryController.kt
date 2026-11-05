package com.example.campsiteapp.controller

import com.example.campsiteapp.data.LotteryStorage
import com.example.campsiteapp.model.Lottery

/**
 * Creates and updates lottery from the details an owner enters
 *
 *
 */
class LotteryController(private val storage: LotteryStorage) {

    /**
     * Validates new lottery and saves it (if valid)
     *
     * @param lottery Lottery entered by owner
     * @return null if lottery was saved, otherwise message with the issue
     */
    fun createLottery(lottery: Lottery): String? {
        val problem = validate(lottery)
        if(problem == null) {
            storage.addLottery(lottery)
        }
        return problem
    }


    /**
     * Validates an edited lottery and saves the changes (if valid)
     *
     * @param lottery Lottery entered by owner
     * @return null if changes were saved, otherwise message with the issue
     */
    fun updateLottery(lottery: Lottery): String? {
        if(lottery.id.isEmpty()) {
            return "This lottery hasn't been created yet"
        }

        val problem = validate(lottery)
        if(problem == null) {
            storage.updateLottery(lottery)
        }
        return problem
    }


    /**
     * Checks a lottery before it is saved to the database
     *
     * Must have: a campsite, both date ranges set and in order, lottery must
     * close before booking period begins,
     */
    fun validate(lottery: Lottery): String? = when {
        lottery.campsiteId.isEmpty() ->
            "Select a campsite"
        lottery.bookingStartDate == 0L || lottery.bookingEndDate == 0L ->
            "Choose the booking period"
        lottery.bookingEndDate < lottery.bookingStartDate ->
            "Booking period must end after it starts"
        lottery.lotteryStartDate == 0L || lottery.lotteryEndDate == 0L ->
            "Choose the lottery period"
        lottery.lotteryEndDate < lottery.lotteryStartDate ->
            "Lottery period must end after it starts"
        lottery.lotteryEndDate > lottery.bookingStartDate ->
            "Lottery must close before booking can begin"
        lottery.numWinners < 1 ->
            "There must be at least one winnter"
        lottery.waitlistCapacity != 0 && lottery.waitlistCapacity < lottery.numWinners ->
            "Waitlist capacity can't be smaller than the number of winners"
        else -> null
    }
}
package com.example.campsiteapp

import com.example.campsiteapp.controller.LotteryController
import com.example.campsiteapp.model.Lottery
import java.time.LocalDate
import java.time.ZoneOffset
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import com.example.campsiteapp.data.LotteryStorage
import org.junit.Assert.assertEquals

class LotteryControllerTest {

    // Epoch millis at UTC midnight, matching what the date range picker returns.
    private fun date(year: Int, month: Int, day: Int): Long =
        LocalDate.of(year, month, day)
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()

    // Registration Nov 10-20, 2026; booking period Dec 1-5, 2026
    private val validLottery = Lottery(
        campsiteIds = listOf("campsiteA"),
        lotteryStartDate = date(2026, 11, 10),
        lotteryEndDate = date(2026, 11, 20),
        bookingStartDate = date(2026, 12, 1),
        bookingEndDate = date(2026, 12, 5),
        numWinners = 2,
        waitlistMax = 5
    )

    @Test
    fun validate_validLottery_returnsNull() {
        assertNull(LotteryController.validate(validLottery))
    }

    @Test
    fun validate_noCampsites_returnsError() {
        assertNotNull(LotteryController.validate(validLottery.copy(campsiteIds = listOf(""))))
    }

    @Test
    fun validate_bookingEndsBeforeItStarts_returnsError() {
        assertNotNull(LotteryController.validate(validLottery.copy(bookingEndDate = date(2026, 11, 30))))
    }

    @Test
    fun validate_registrationEndsBeforeItStarts_returnsError() {
        assertNotNull(LotteryController.validate(validLottery.copy(lotteryEndDate = date(2026, 11, 9))))
    }

    @Test
    fun validate_registrationClosesAfterBookingStarts_returnsError() {
        assertNotNull(LotteryController.validate(validLottery.copy(lotteryEndDate = date(2026, 12, 3))))
    }

    @Test
    fun validate_registrationClosesOnBookingStartDate_isValid() {
        assertNull(LotteryController.validate(validLottery.copy(lotteryEndDate = date(2026, 12, 1))))
    }

    @Test
    fun validate_zeroWinners_returnsError() {
        assertNotNull(LotteryController.validate(validLottery.copy(numWinners = 0)))
    }

    @Test
    fun validate_capacityBelowWinners_returnsError() {
        assertNotNull(LotteryController.validate(validLottery.copy(waitlistMax = 1)))
    }

//    @Test
//    fun validate_nullCapacity_meansNoLimitAndIsValid() {
//        assertNull(LotteryController.validate(validLottery.copy(waitlistMax = null)))
//    }

    @Test
    fun createLottery_validLottery_savesItAndReturnsNull() {
        val storage = FakeLotteryStorage()

        val result = LotteryController(storage).createLottery(validLottery)

        assertNull(result)
        assertEquals(listOf(validLottery), storage.added)
    }

    @Test
    fun createLottery_invalidLottery_returnsErrorAndSavesNothing() {
        val storage = FakeLotteryStorage()

        val result = LotteryController(storage).createLottery(validLottery.copy(numWinners = 0))

        assertNotNull(result)
        assertEquals(emptyList<Lottery>(), storage.added)
    }

    @Test
    fun updateLottery_validLottery_savesChangesAndReturnsNull() {
        val storage = FakeLotteryStorage()
        val edited = validLottery.copy(id = "lottery1", numWinners = 3)

        val result = LotteryController(storage).updateLottery(edited)

        assertNull(result)
        assertEquals(listOf(edited), storage.updated)
    }

    @Test
    fun updateLottery_invalidLottery_returnsErrorAndSavesNothing() {
        val storage = FakeLotteryStorage()

        val result = LotteryController(storage).updateLottery(
            validLottery.copy(id = "lottery1", campsiteIds = listOf(""))
        )

        assertNotNull(result)
        assertEquals(emptyList<Lottery>(), storage.updated)
    }

    @Test
    fun updateLottery_lotteryWithoutId_returnsErrorAndSavesNothing() {
        val storage = FakeLotteryStorage()

        val result = LotteryController(storage).updateLottery(validLottery)

        assertNotNull(result)
        assertEquals(emptyList<Lottery>(), storage.updated)
    }
}

// A stand-in for LotteryDatabase that records saves instead of talking to Firebase.
private class FakeLotteryStorage : LotteryStorage {
    val added = mutableListOf<Lottery>()
    val updated = mutableListOf<Lottery>()

    override fun addLottery(lottery: Lottery) { added.add(lottery) }
    override fun updateLottery(lottery: Lottery) { updated.add(lottery) }
}
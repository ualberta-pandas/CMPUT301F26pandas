package com.example.campsiteapp.view

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.campsiteapp.model.Campsite
import com.example.campsiteapp.model.Lottery
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/**
 * Tests for the owner's lottery form and list (US 02.04.04: owner sets a booking period).
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class LotteryFormTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val campsite = Campsite(id = "campsiteA", name = "Pine Ridge", ownerId = "owner1")

    private val existingLottery = Lottery(
        id = "lottery1",
        ownerId = "owner1",
        campsiteIds = listOf("campsiteA"),
        campsite = campsite,
        lotteryStartDate = "2026-11-10",
        lotteryEndDate = "2026-11-20",
        bookingStartDate = "2026-12-01",
        bookingEndDate = "2026-12-05",
        numWinners = 1,
        waitlistMax = 5,
        waitlist = arrayListOf("entrant1", "entrant2"),
        winners = arrayListOf("entrant1"),
        status = "closed"
    )

    /** Shows the form and returns a holder that captures what Save passes on. */
    private fun setForm(initial: Lottery?): Array<Lottery?> {
        val saved = arrayOfNulls<Lottery>(1)
        composeRule.setContent {
            LotteryForm(
                initial = initial,
                campsites = listOf(campsite),
                onSave = { saved[0] = it; null },
                onClose = {}
            )
        }
        return saved
    }

    @Test
    fun editing_keepsBookingPeriodWaitlistAndWinners() {
        val saved = setForm(existingLottery)

        composeRule.onNodeWithTag("saveLotteryButton").performScrollTo().performClick()

        val lottery = saved[0]
        assertNotNull(lottery)
        assertEquals("2026-12-01", lottery!!.bookingStartDate)
        assertEquals("2026-12-05", lottery.bookingEndDate)
        assertEquals(listOf("entrant1", "entrant2"), lottery.waitlist)
        assertEquals(listOf("entrant1"), lottery.winners)
        assertEquals("closed", lottery.status)
        assertEquals("lottery1", lottery.id)
    }

    @Test
    fun saving_setsEmbeddedCampsiteAndOwnerFromSelectedCampsite() {
        val saved = setForm(null)

        composeRule.onNodeWithTag("campsiteCheckbox-campsiteA").performScrollTo().performClick()
        composeRule.onNodeWithTag("saveLotteryButton").performScrollTo().performClick()

        val lottery = saved[0]
        assertNotNull(lottery)
        assertEquals(campsite, lottery!!.campsite)
        assertEquals("owner1", lottery.ownerId)
    }

    @Test
    fun lotteryCard_unlimitedWaitlist_showsNoLimit() {
        composeRule.setContent {
            LotteryOwnerScreen().LotteryList(
                lotteries = listOf(existingLottery.copy(waitlistMax = 0)),
                campsites = listOf(campsite),
                onCreateLottery = { null },
                onUpdateLottery = { null }
            )
        }

        composeRule.onNodeWithTag("lotteryWaitlist", useUnmergedTree = true)
            .assertTextEquals("Waitlist: No limit")
    }
}

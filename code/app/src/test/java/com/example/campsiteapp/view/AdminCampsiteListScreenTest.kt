package com.example.campsiteapp.view

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.campsiteapp.model.Campsite
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/**
 * Tests for browsing and removing campsites as an admin.
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class AdminCampsiteListScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val pineRidge = Campsite(
        id = "1",
        name = "Pine Ridge",
        location = "Jasper",
        occupancy = 6.0,
        owner = "Sam",
        rules = "No fires",
    )
    private val lakeview = Campsite(id = "2", name = "Lakeview", location = "Banff", occupancy = 4.0, owner = "Ana")

    private val removed = mutableListOf<Campsite>()
    private var backPressed = false

    private fun setScreen() {
        composeRule.setContent {
            AdminCampsiteListScreen(
                campsites = listOf(pineRidge, lakeview),
                onRemoveCampsite = { removed.add(it) },
                onBack = { backPressed = true },
            )
        }
    }

    @Test
    fun list_showsEveryCampsiteWithOwnerAndOccupancy() {
        setScreen()

        composeRule.onNodeWithText("Pine Ridge").assertExists()
        composeRule.onNodeWithText("Owned by Sam", substring = true).assertExists()
        composeRule.onNodeWithText("6 people", substring = true).assertExists()
        composeRule.onNodeWithText("Lakeview").assertExists()
        composeRule.onNodeWithText("Owned by Ana", substring = true).assertExists()
    }

    @Test
    fun tappingCampsite_opensItsDetails() {
        setScreen()

        composeRule.onNodeWithText("Pine Ridge").performClick()

        composeRule.onNodeWithText("No fires").assertExists()
    }

    /** Removing from the second campsite's details removes that campsite, not the first one in the list. */
    @Test
    fun confirmingRemoval_removesTheTappedCampsiteAndClosesDetails() {
        setScreen()

        composeRule.onNodeWithText("Lakeview").performClick()
        composeRule.onNodeWithTag("removeButton").performClick()
        composeRule.onNodeWithTag("confirmRemoveButton").performClick()

        assertEquals(listOf(lakeview), removed)
        composeRule.onAllNodesWithText("Rules and Cancellation Policy").assertCountEquals(0)
    }

    @Test
    fun backButton_callsOnBack() {
        setScreen()

        composeRule.onNodeWithTag("backButton").performClick()

        assertTrue(backPressed)
    }
}

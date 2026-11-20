package com.example.campsiteapp.view

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso

import com.example.campsiteapp.model.Campsite
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class OwnerCampsiteListScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun fakeCampsite(
        id: String = "1",
        name: String = "Pine Ridge",
        location: String = "Edmonton",
        occupancy: Double = 4.0,
        status: String = "Available"
    ) = Campsite(
        id = id,
        name = name,
        latitude = "53.5461",
        longitude = "-113.4938",
        image = "",
        location = location,
        occupancy = occupancy,
        owner = "John",
        price = "50",
        startDate = "2026/12/01",
        endDate = "2026/12/10",
        description = "Quiet place near a lake",
        tags = "lake,quiet",
        rules = "No loud music",
        status = status
    )

    private fun setScreen(
        campsites: List<Campsite> = listOf(fakeCampsite())
    ) {
        composeRule.setContent {
            OwnerCampsiteListScreen().CampsitesList(
                campsites = campsites,
                onAddCampsite = {},
                onEditCampsite = { _, _ -> }
            )
        }
    }

    private fun twoCampsites() = listOf(
        fakeCampsite(id = "1", name = "Pine Ridge"),
        fakeCampsite(id = "2", name = "Lakeview")
    )

    // ---------- List shows the campsites ----------
    // The rows are clickable, so the texts inside them are only
    // found with useUnmergedTree = true.

    @Test
    fun list_showsCampsiteInfo() {
        setScreen()

        composeRule.onNodeWithTag("campsiteName_0", useUnmergedTree = true)
            .assertTextEquals("Pine Ridge")
        composeRule.onNodeWithTag("campsiteLocation_0", useUnmergedTree = true)
            .assertTextEquals("Edmonton")
        composeRule.onNodeWithTag("campsiteOccupancy_0", useUnmergedTree = true)
            .assertTextEquals("4.0")
        composeRule.onNodeWithTag("campsiteStatus_0", useUnmergedTree = true)
            .assertTextEquals("Available")
        composeRule.onNodeWithTag("campsiteSlots_0", useUnmergedTree = true)
            .assertTextEquals("# Slots available")
    }

    @Test
    fun list_showsAllCampsites() {
        setScreen(twoCampsites())

        composeRule.onNodeWithTag("campsiteName_0", useUnmergedTree = true)
            .assertTextEquals("Pine Ridge")
        composeRule.onNodeWithTag("campsiteName_1", useUnmergedTree = true)
            .assertTextEquals("Lakeview")
    }

    @Test
    fun list_showsImageForEachCampsite() {
        setScreen(twoCampsites())

        composeRule.onNodeWithTag("campsiteImage_0", useUnmergedTree = true).assertExists()
        composeRule.onNodeWithTag("campsiteImage_1", useUnmergedTree = true).assertExists()
    }

    @Test
    fun emptyList_showsNoRows_butShowsAddButton() {
        setScreen(emptyList())

        composeRule.onAllNodesWithTag("campsiteRow_0").assertCountEquals(0)
        composeRule.onNodeWithTag("addCampsiteButton").assertIsDisplayed()
    }

    // ---------- Details pop-up ----------

    @Test
    fun clickingCampsite_opensDetailsPopup() {
        setScreen()

        composeRule.onNodeWithTag("campsiteRow_0").performClick()

        composeRule.onNodeWithTag("detailsTitle")
            .assertTextEquals("Pine Ridge Details Screen")
    }

    @Test
    fun detailsPopup_closeButton_closesIt() {
        setScreen()

        composeRule.onNodeWithTag("campsiteRow_0").performClick()
        composeRule.onNodeWithTag("detailsTitle").assertIsDisplayed()

        composeRule.onNodeWithTag("closeButton").performClick()

        composeRule.onAllNodesWithTag("detailsTitle").assertCountEquals(0)
    }

    @Test
    fun clickingSecondCampsite_showsItsOwnDetails() {
        setScreen(twoCampsites())

        composeRule.onNodeWithTag("campsiteRow_1").performClick()

        composeRule.onNodeWithTag("detailsTitle")
            .assertTextEquals("Lakeview Details Screen")
    }

    @Test
    fun detailsPopup_canBeOpenedAgainAfterClosing() {
        setScreen()

        composeRule.onNodeWithTag("campsiteRow_0").performClick()
        composeRule.onNodeWithTag("closeButton").performClick()
        composeRule.onNodeWithTag("campsiteRow_0").performClick()

        composeRule.onNodeWithTag("detailsTitle")
            .assertTextEquals("Pine Ridge Details Screen")
    }

    // ---------- Registration pop-up ----------

    @Test
    fun plusButton_opensRegistrationPopup() {
        setScreen()

        composeRule.onAllNodesWithTag("registrationDialog").assertCountEquals(0)

        composeRule.onNodeWithTag("addCampsiteButton").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("registrationDialog").assertIsDisplayed()
    }

    @Test
    fun registrationPopup_closesOnBackPress() {
        setScreen()

        composeRule.onNodeWithTag("addCampsiteButton").performClick()
        composeRule.onNodeWithTag("registrationDialog").assertIsDisplayed()

        Espresso.pressBack()
        composeRule.waitForIdle()

        composeRule.onAllNodesWithTag("registrationDialog").assertCountEquals(0)
    }
}
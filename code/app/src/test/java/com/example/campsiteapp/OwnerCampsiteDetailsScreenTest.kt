package com.example.campsiteapp

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick

import com.example.campsiteapp.model.Campsite
import com.example.campsiteapp.view.OwnerCampsiteDetailsScreen
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class OwnerCampsiteDetailsScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun fakeCampsite() = Campsite(
        id = "1",
        name = "Pine Ridge",
        latitude = "53.5461",
        longitude = "-113.4938",
        image = "",
        location = "Edmonton",
        occupancy = 4.0,
        owner = "John",
        price = "50",
        startDate = "2026/12/01",
        endDate = "2026/12/10",
        description = "Quiet place near a lake",
        tags = "lake,quiet",
        rules = "No loud music",
        status = "Available"
    )

    private fun setScreen(
        campsite: Campsite? = fakeCampsite(),
        onDismiss: () -> Unit = {}
    ) {
        composeRule.setContent {
            OwnerCampsiteDetailsScreen().CampsiteDetailsScreen(
                onDismissRequest = onDismiss,
                selectedCampsiteDetails = campsite,
                onEditCampsite = { _, _ -> }
            )
        }
    }

    // ---------- It shows the clicked campsite ----------

    @Test
    fun title_showsCampsiteName() {
        setScreen()
        composeRule.onNodeWithTag("detailsTitle")
            .assertTextEquals("Pine Ridge Details Screen")
    }

    @Test
    fun fields_showCampsiteValues() {
        setScreen()
        composeRule.onNodeWithTag("campName").assertTextContains("Pine Ridge")
        composeRule.onNodeWithTag("location").assertTextContains("Edmonton")
        composeRule.onNodeWithTag("occupancy").assertTextContains("4.0")
        composeRule.onNodeWithTag("owner").assertTextContains("John")
        composeRule.onNodeWithTag("price").assertTextContains("50")
        composeRule.onNodeWithTag("availableDates")
            .assertTextContains("2026/12/01-2026/12/10")
        composeRule.onNodeWithTag("description")
            .assertTextContains("Quiet place near a lake")
        composeRule.onNodeWithTag("tags").assertTextContains("lake,quiet")
        composeRule.onNodeWithTag("rules").assertTextContains("No loud music")
        composeRule.onNodeWithTag("status").assertTextContains("Available")
    }

    @Test
    fun mapAndImage_areShown() {
        setScreen()
        composeRule.onNodeWithTag("campsiteMap").assertExists()
        composeRule.onNodeWithTag("campsiteImage").assertExists()
    }

    // ---------- Close button ----------

    @Test
    fun closeButton_callsOnDismissRequest() {
        var dismissed = false
        setScreen(onDismiss = { dismissed = true })

        composeRule.onNodeWithTag("closeButton").performClick()

        assertTrue(dismissed)
    }

    // ---------- View Dates pop-up ----------

    @Test
    fun viewDates_opensPicker_andCancelClosesIt() {
        setScreen()

        composeRule.onAllNodesWithTag("dateRangePicker").assertCountEquals(0)

        composeRule.onNodeWithTag("viewDatesButton").performClick()
        composeRule.onNodeWithTag("dateRangePicker").assertIsDisplayed()

        composeRule.onNodeWithTag("datePickerCancel").performClick()
        composeRule.onAllNodesWithTag("dateRangePicker").assertCountEquals(0)
    }

    @Test
    fun viewDates_okClosesPicker() {
        setScreen()

        composeRule.onNodeWithTag("viewDatesButton").performClick()
        composeRule.onNodeWithTag("datePickerOk").performClick()

        composeRule.onAllNodesWithTag("dateRangePicker").assertCountEquals(0)
    }

    // ---------- Share and Edit only open pop-ups ----------

    @Test
    fun shareButton_opensSharePopup() {
        setScreen()

        composeRule.onAllNodesWithTag("shareDialog").assertCountEquals(0)

        composeRule.onNodeWithTag("shareButton").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("shareDialog").assertIsDisplayed()
    }

    @Test
    fun editButton_opensEditPopup() {
        setScreen()

        composeRule.onAllNodesWithTag("editDialog").assertCountEquals(0)

        composeRule.onNodeWithTag("editButton").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("editDialog").assertIsDisplayed()
    }

    // ---------- No campsite ----------

    @Test
    fun nullCampsite_sharePopupDoesNotOpen() {
        setScreen(campsite = null)

        composeRule.onNodeWithTag("shareButton").performClick()
        composeRule.waitForIdle()

        composeRule.onAllNodesWithTag("shareDialog").assertCountEquals(0)
    }

    @Test
    fun nullCampsite_editPopupDoesNotOpen() {
        setScreen(campsite = null)

        composeRule.onNodeWithTag("editButton").performClick()
        composeRule.waitForIdle()

        composeRule.onAllNodesWithTag("editDialog").assertCountEquals(0)
    }
}
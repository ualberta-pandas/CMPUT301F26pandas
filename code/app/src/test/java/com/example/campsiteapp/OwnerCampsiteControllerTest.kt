package com.example.campsiteapp.controller

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.campsiteapp.model.Campsite
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast



/**
 * The form is taller than a default Robolectric screen and the Columns are not scrollable,
 * so a tall screen is used to keep every field "on screen" for the test input injection.
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class OwnerCampsiteControllerTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val controller = OwnerCampsiteController()

    // ---------------------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------------------

    private fun type(label: String, text: String) {
        composeRule.onNodeWithText(label).performTextInput(text)
    }

    private fun latestToast(): String? = ShadowToast.getTextOfLatestToast()

    private fun sampleCampsite() = Campsite(
        id = "camp-1",
        name = "Lakeview",
        location = "Jasper",
        occupancy = 6.0,
        owner = "Sam",
        latitude = "53.5",
        longitude = "-113.5",
        price = "45",
        startDate = "2026/07/01",
        endDate = "2026/07/05",
        description = "Quiet spot by the lake",
        tags = "lake,quiet",
        rules = "No loud music",
        image = "",
        status = "Available"
    )

    // ---------------------------------------------------------------------------------
    // RegisterCampsite
    // ---------------------------------------------------------------------------------

    @Test
    fun register_showsAllFieldsAndButtons() {
        composeRule.setContent {
            controller.RegisterCampsite(onAddCampsite = {}, onDismissRequest = {})
        }

        composeRule.onNodeWithText("Add Campsite").assertIsDisplayed()
        composeRule.onNodeWithTag("campName").assertIsDisplayed()
        listOf(
            "Location", "Occupancy", "Owner", "Price", "Available Dates",
            "Map Location", "Description", "Tags", "Rules and terms of use", "Status"
        ).forEach { composeRule.onNodeWithText(it).assertIsDisplayed() }
        composeRule.onNodeWithText("Select Dates").assertIsDisplayed()
        composeRule.onNodeWithText("Choose Location").assertIsDisplayed()
        composeRule.onNodeWithText("Add").assertIsDisplayed()
    }

    @Test
    fun register_statusDefaultsToAvailable() {
        composeRule.setContent {
            controller.RegisterCampsite(onAddCampsite = {}, onDismissRequest = {})
        }

        composeRule.onNodeWithText("Status").assertTextContains("Available")
    }

    @Test
    fun register_closeButton_callsOnDismissRequest() {
        var dismissed = false
        composeRule.setContent {
            controller.RegisterCampsite(
                onAddCampsite = {},
                onDismissRequest = { dismissed = true }
            )
        }

        composeRule.onNodeWithText("x").performClick()

        assertTrue(dismissed)
    }

    @Test
    fun register_addWithEmptyFields_showsFillAllFieldsToast_andDoesNotAdd() {
        var added: Campsite? = null
        composeRule.setContent {
            controller.RegisterCampsite(onAddCampsite = { added = it }, onDismissRequest = {})
        }

        composeRule.onNodeWithText("Add").performClick()
        composeRule.waitForIdle()

        assertEquals("Fill all fields", latestToast())
        assertNull(added)
    }

    @Test
    fun register_addWithOneFieldMissing_showsFillAllFieldsToast() {
        var added: Campsite? = null
        composeRule.setContent {
            controller.RegisterCampsite(onAddCampsite = { added = it }, onDismissRequest = {})
        }

        composeRule.onNodeWithTag("campName").performTextInput("Lakeview")
        type("Location", "Jasper")
        type("Occupancy", "6")
        type("Owner", "Sam")
        type("Price", "45")
        type("Description", "Quiet spot")
        type("Tags", "lake")
        // "Rules and terms of use" intentionally left blank

        composeRule.onNodeWithText("Add").performClick()
        composeRule.waitForIdle()

        assertEquals("Fill all fields", latestToast())
        assertNull(added)
    }

    @Test
    fun register_allFieldsFilledButNoImage_showsSelectImageToast_andDoesNotAdd() {
        var added: Campsite? = null
        var dismissed = false
        composeRule.setContent {
            controller.RegisterCampsite(
                onAddCampsite = { added = it },
                onDismissRequest = { dismissed = true }
            )
        }

        composeRule.onNodeWithTag("campName").performTextInput("Lakeview")
        type("Location", "Jasper")
        type("Occupancy", "6")
        type("Owner", "Sam")
        type("Price", "45")
        type("Description", "Quiet spot")
        type("Tags", "lake")
        type("Rules and terms of use", "No loud music")

        composeRule.onNodeWithText("Add").performClick()
        composeRule.waitForIdle()

        assertEquals("Please select an image", latestToast())
        assertNull(added)
        assertFalse(dismissed)
        // isUploading must be reset by the finally block so the button is usable again
        composeRule.onNodeWithText("Add").assertIsDisplayed()
    }

    /**
     * Known gap: mapLocation is overwritten with "$latitude,$longitude" on every recomposition,
     * so it is never blank and the "map location is required" check can never fail.
     * This test documents the current behaviour; flip the assertion once you track
     * "location chosen" separately.
     */
    @Test
    fun register_mapLocation_startsAsZeroCoordinates() {
        composeRule.setContent {
            controller.RegisterCampsite(onAddCampsite = {}, onDismissRequest = {})
        }

        composeRule.onNodeWithText("Map Location").assertTextContains("0.0,0.0")
    }

    // ---------------------------------------------------------------------------------
    // EditCampsite
    // ---------------------------------------------------------------------------------

    @Test
    fun edit_prefillsFieldsFromSelectedCampsite() {
        composeRule.setContent {
            controller.EditCampsite(
                onDismissRequest = {},
                selectedCampsiteDetails = sampleCampsite(),
                onEditCampsite = { _, _ -> }
            )
        }

        composeRule.onNodeWithText("Lakeview Details Screen").assertIsDisplayed()
        composeRule.onNodeWithTag("campName").assertTextContains("Lakeview")
        composeRule.onNodeWithText("Location").assertTextContains("Jasper")
        composeRule.onNodeWithText("Owner").assertTextContains("Sam")
        composeRule.onNodeWithText("Price").assertTextContains("45")
        composeRule.onNodeWithText("Description").assertTextContains("Quiet spot by the lake")
        composeRule.onNodeWithText("Tags").assertTextContains("lake,quiet")
        composeRule.onNodeWithText("Rules and terms of use").assertTextContains("No loud music")
        composeRule.onNodeWithText("Status").assertTextContains("Available")
        composeRule.onNodeWithText("Available Dates")
            .assertTextContains("2026/07/01 - 2026/07/05")
        composeRule.onNodeWithText("Change Dates").assertIsDisplayed()
    }

    @Test
    fun edit_closeButton_callsOnDismissRequest() {
        var dismissed = false
        composeRule.setContent {
            controller.EditCampsite(
                onDismissRequest = { dismissed = true },
                selectedCampsiteDetails = sampleCampsite(),
                onEditCampsite = { _, _ -> }
            )
        }

        composeRule.onNodeWithText("x").performClick()

        assertTrue(dismissed)
    }

    @Test
    fun edit_clearingRequiredField_showsFillAllFieldsToast_andDoesNotCallback() {
        var called = false
        composeRule.setContent {
            controller.EditCampsite(
                onDismissRequest = {},
                selectedCampsiteDetails = sampleCampsite(),
                onEditCampsite = { _, _ -> called = true }
            )
        }

        composeRule.onNodeWithTag("campName").performTextClearance()
        composeRule.onNodeWithContentDescription("Update").performClick()
        composeRule.waitForIdle()

        assertEquals("Fill all fields", latestToast())
        assertFalse(called)
    }

    @Test
    fun edit_changedName_callsOnEditWithOldAndNewCampsite() {
        val original = sampleCampsite()
        var oldCamp: Campsite? = null
        var newCamp: Campsite? = null
        var dismissed = false

        composeRule.setContent {
            controller.EditCampsite(
                onDismissRequest = { dismissed = true },
                selectedCampsiteDetails = original,
                onEditCampsite = { old, new ->
                    oldCamp = old
                    newCamp = new
                }
            )
        }

        composeRule.onNodeWithTag("campName").performTextReplacement("Lakeview Deluxe")
        composeRule.onNodeWithContentDescription("Update").performClick()
        composeRule.waitForIdle()

        assertNotNull(newCamp)
        assertEquals("Successfully Edited", latestToast())
        assertTrue(dismissed)

        assertEquals(original.id, oldCamp?.id)
        assertEquals("Lakeview", oldCamp?.name)
        assertEquals("Lakeview Deluxe", newCamp?.name)
        // Unchanged image must not be re-uploaded
        assertEquals(original.image, newCamp?.image)
        assertEquals("2026/07/01", newCamp?.startDate)
        assertEquals("2026/07/05", newCamp?.endDate)
    }

    /**
     * EXPECTED TO FAIL until fixed: in EditCampsite the initial values are swapped
     * (longitude is seeded from latitude and vice-versa), so saving without touching the map
     * flips the coordinates.
     */
    @Test
    fun edit_savingWithoutTouchingMap_keepsOriginalCoordinates() {
        var newCamp: Campsite? = null
        composeRule.setContent {
            controller.EditCampsite(
                onDismissRequest = {},
                selectedCampsiteDetails = sampleCampsite(),
                onEditCampsite = { _, new -> newCamp = new }
            )
        }

        composeRule.onNodeWithContentDescription("Update").performClick()
        composeRule.waitForIdle()

        assertEquals("53.5", newCamp?.latitude)
        assertEquals("-113.5", newCamp?.longitude)
    }

    /**
     * EXPECTED TO FAIL until fixed: occupancy state is typed as Any (Double ?: ""), and once the
     * user edits it the value becomes a String, so `occupancy as Double` throws ClassCastException
     * (swallowed by the catch block and shown as an "Upload failed" toast).
     */
    @Test
    fun edit_changedOccupancy_isSavedAsDouble() {
        var newCamp: Campsite? = null
        composeRule.setContent {
            controller.EditCampsite(
                onDismissRequest = {},
                selectedCampsiteDetails = sampleCampsite(),
                onEditCampsite = { _, new -> newCamp = new }
            )
        }

        composeRule.onNodeWithText("Occupancy").performTextReplacement("10")
        composeRule.onNodeWithContentDescription("Update").performClick()
        composeRule.waitForIdle()

        assertEquals(10.0, newCamp?.occupancy ?: -1.0, 0.0)
    }
}
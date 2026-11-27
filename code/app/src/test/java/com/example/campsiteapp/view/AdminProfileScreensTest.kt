package com.example.campsiteapp.view

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.campsiteapp.model.User
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/**
 * Tests for browsing profiles and removing a profile as an admin.
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class AdminProfileScreensTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun user(deviceId: String, name: String, vararg roles: String) = User().apply {
        this.deviceId = deviceId
        this.name = name
        this.roles = roles.toMutableList()
    }

    private val alice = user("device-a", "Alice", "entrant", "owner").apply {
        email = "alice@example.com"
        phone = "780-555-0100"
        notificationsEnabled = false
    }
    private val bob = user("device-b", "Bob", "entrant")

    @Test
    fun profileList_showsNamesAndRolesAndPassesTappedProfile() {
        var tapped: User? = null
        composeRule.setContent {
            AdminProfileListScreen(profiles = listOf(alice, bob), onProfileClick = { tapped = it }, onBack = {})
        }

        composeRule.onNodeWithText("entrant, owner", substring = true).assertExists()
        composeRule.onNodeWithText("Bob", substring = true).performClick()

        assertEquals(bob, tapped)
    }

    @Test
    fun profileScreen_showsContactDetailsAndNotificationPreference() {
        composeRule.setContent {
            AdminProfileScreen(profile = alice, ownedCampsiteCount = 0, onRemove = {}, onBack = {})
        }

        composeRule.onNodeWithText("Alice").assertExists()
        composeRule.onNodeWithText("alice@example.com").assertExists()
        composeRule.onNodeWithText("780-555-0100").assertExists()
        composeRule.onNodeWithText("Opted out").assertExists()
    }

    /** Removing an owner also removes their campsites, so the confirmation must say how many. */
    @Test
    fun removingOwner_warnsHowManyCampsitesGoWithThem() {
        composeRule.setContent {
            AdminProfileScreen(profile = alice, ownedCampsiteCount = 2, onRemove = {}, onBack = {})
        }

        composeRule.onNodeWithTag("removeButton").performClick()

        composeRule.onNodeWithText("2 campsite listing", substring = true).assertExists()
    }

    /** Users without campsites get the plain confirmation instead of the owner warning. */
    @Test
    fun removingNonOwner_doesNotMentionCampsites() {
        composeRule.setContent {
            AdminProfileScreen(profile = bob, ownedCampsiteCount = 0, onRemove = {}, onBack = {})
        }

        composeRule.onNodeWithTag("removeButton").performClick()

        composeRule.onNodeWithText("Are you sure you want to remove this profile?").assertExists()
    }

    @Test
    fun confirmingRemoval_callsOnRemove() {
        var removed = false
        composeRule.setContent {
            AdminProfileScreen(profile = bob, ownedCampsiteCount = 0, onRemove = { removed = true }, onBack = {})
        }

        composeRule.onNodeWithTag("removeButton").performClick()
        composeRule.onNodeWithTag("confirmRemoveButton").performClick()

        assertTrue(removed)
    }
}

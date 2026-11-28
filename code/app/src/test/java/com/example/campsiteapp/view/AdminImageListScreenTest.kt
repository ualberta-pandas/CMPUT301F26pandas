package com.example.campsiteapp.view

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.campsiteapp.controller.LoggedImage
import com.example.campsiteapp.model.User
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/**
 * Tests for browsing uploaded images and removing them as an admin.
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class AdminImageListScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val alice = User().apply {
        deviceId = "device-a"
        name = "Alice"
    }
    private val unnamed = User().apply { deviceId = "device-unnamed" }

    private val aliceImage = LoggedImage(id = "log-1", url = "https://example.com/a.jpg", uploaderId = "device-a")
    private val orphanImage = LoggedImage(id = "log-2", url = "https://example.com/b.jpg", uploaderId = "device-gone")

    private var tappedUploader: User? = null
    private val removed = mutableListOf<LoggedImage>()

    private fun setScreen(images: List<LoggedImage>, profiles: List<User>) {
        composeRule.setContent {
            AdminImageListScreen(
                images = images,
                profiles = profiles,
                onUploaderClick = { tappedUploader = it },
                onRemoveImage = { removed.add(it) },
                onBack = {},
            )
        }
    }

    @Test
    fun uploaderWithProfile_isNamedAndOpensTheirProfile() {
        setScreen(listOf(aliceImage), listOf(alice))

        composeRule.onNodeWithText("Alice", substring = true).performClick()

        assertEquals(alice, tappedUploader)
    }

    /** An image whose uploader's profile was removed still lists them by device ID, but has no profile to open. */
    @Test
    fun uploaderWithoutProfile_showsDeviceIdAndCannotBeOpened() {
        setScreen(listOf(orphanImage), listOf(alice))

        composeRule.onNodeWithText("device-gone", substring = true).performClick()

        assertNull(tappedUploader)
    }

    /** A profile that never set a name shows the device ID rather than an empty uploader. */
    @Test
    fun uploaderWithBlankName_fallsBackToDeviceId() {
        setScreen(listOf(aliceImage.copy(uploaderId = "device-unnamed")), listOf(unnamed))

        composeRule.onNodeWithText("device-unnamed", substring = true).assertExists()
    }

    /** With several images listed, each remove button removes its own image only. */
    @Test
    fun confirmingRemoval_removesOnlyThatImage() {
        setScreen(listOf(aliceImage, orphanImage), listOf(alice))

        composeRule.onAllNodesWithTag("removeButton")[1].performClick()
        composeRule.onNodeWithTag("confirmRemoveButton").performClick()

        assertEquals(listOf(orphanImage), removed)
    }
}

package com.example.campsiteapp.view.components

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/**
 * Tests for [RemoveButton], which every admin removal goes through.
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class RemoveButtonTest {

    @get:Rule
    val composeRule = createComposeRule()

    private var confirmations = 0

    @Before
    fun setUp() {
        composeRule.setContent {
            RemoveButton(title = "Remove Thing", message = "Really remove it?", onConfirm = { confirmations++ })
        }
    }

    @Test
    fun tappingRemove_asksForConfirmationWithoutRemoving() {
        composeRule.onNodeWithTag("removeButton").performClick()

        composeRule.onNodeWithText("Remove Thing").assertExists()
        composeRule.onNodeWithText("Really remove it?").assertExists()
        assertEquals(0, confirmations)
    }

    @Test
    fun confirming_removesOnceAndClosesDialog() {
        composeRule.onNodeWithTag("removeButton").performClick()
        composeRule.onNodeWithTag("confirmRemoveButton").performClick()

        assertEquals(1, confirmations)
        composeRule.onAllNodesWithText("Really remove it?").assertCountEquals(0)
    }

    @Test
    fun cancelling_closesDialogWithoutRemoving() {
        composeRule.onNodeWithTag("removeButton").performClick()
        composeRule.onNodeWithText("Cancel").performClick()

        assertEquals(0, confirmations)
        composeRule.onAllNodesWithText("Really remove it?").assertCountEquals(0)
    }
}

/*
 * Screen layout with a centred title and an optional round Back button.
 */
package com.example.campsiteapp.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.campsiteapp.R

/**
 * A screen with a top bar.
 *
 * @param title text shown in the top bar.
 * @param onBack called when Back is pressed; no Back button is shown if `null`.
 * @param actions content shown at the end of the top bar.
 * @param content screen body; must apply the given padding.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenScaffold(
    title: String,
    onBack: (() -> Unit)?,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(title) },
                navigationIcon = { if (onBack != null) BackButton(onBack) },
                actions = actions,
            )
        },
        content = content,
    )
}

/**
 * Grey circle with a white "<", as in the admin mockups.
 */
@Composable
private fun BackButton(onClick: () -> Unit) {
    val description = stringResource(R.string.back)
    Box(
        modifier = Modifier
            .padding(start = 12.dp)
            .size(36.dp)
            .clip(CircleShape)
            .background(Color.Gray)
            .clickable(role = Role.Button, onClick = onClick)
            .semantics { contentDescription = description }
            .testTag("backButton"),
        contentAlignment = Alignment.Center,
    ) {
        Text("<", color = Color.White, fontWeight = FontWeight.Bold)
    }
}

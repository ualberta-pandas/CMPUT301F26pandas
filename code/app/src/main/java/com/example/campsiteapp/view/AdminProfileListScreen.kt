/*
 * Admin list of all profiles with their roles. Tapping one opens the profile.
 */
package com.example.campsiteapp.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.campsiteapp.R
import com.example.campsiteapp.User
import com.example.campsiteapp.view.components.ScreenScaffold

/**
 * Screen listing every profile.
 *
 * @param profiles profiles to show.
 * @param onProfileClick called when a profile is tapped.
 * @param onBack called when Back is pressed.
 */
@Composable
fun AdminProfileListScreen(profiles: List<User>, onProfileClick: (User) -> Unit, onBack: () -> Unit) {
    ScreenScaffold(title = stringResource(R.string.profiles_title), onBack = onBack) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(profiles, key = { it.deviceId }) { profile ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onProfileClick(profile) }
                        .padding(16.dp),
                ) {
                    Text(profile.name, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                }
                HorizontalDivider()
            }
        }
    }
}

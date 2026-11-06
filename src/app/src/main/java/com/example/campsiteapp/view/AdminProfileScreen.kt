/*
 * Admin view of one profile, with a button to remove it. Removing an owner also
 * removes their campsites.
 */
package com.example.campsiteapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.campsiteapp.R
import com.example.campsiteapp.model.User
import com.example.campsiteapp.view.components.LabelledField
import com.example.campsiteapp.view.components.RemoveButton
import com.example.campsiteapp.view.components.ScreenScaffold

/**
 * Profile details screen.
 *
 * @param profile profile to show.
 * @param ownedCampsiteCount number of campsites the user owns, which are removed with them.
 * @param onRemove called when the admin confirms removing the profile.
 * @param onBack called when Back is pressed.
 */
@Composable
fun AdminProfileScreen(profile: User, ownedCampsiteCount: Int, onRemove: () -> Unit, onBack: () -> Unit) {
    val removeMessage = if (ownedCampsiteCount > 0) {
        stringResource(R.string.remove_owner_message, ownedCampsiteCount)
    } else {
        stringResource(R.string.remove_profile_message)
    }

    ScreenScaffold(
        title = stringResource(R.string.view_profile_title),
        onBack = onBack,
        actions = {
            RemoveButton(
                title = stringResource(R.string.remove_profile_title),
                message = removeMessage,
                onConfirm = onRemove,
                modifier = Modifier.padding(end = 12.dp),
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AsyncImage(
                model = profile.profileImage.ifEmpty { null },
                contentDescription = stringResource(R.string.profile_image),
                modifier = Modifier.size(96.dp).clip(CircleShape).background(Color.LightGray),
                contentScale = ContentScale.Crop,
            )
            Text(profile.name, style = MaterialTheme.typography.headlineSmall)
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                LabelledField(stringResource(R.string.label_email), profile.email)
                LabelledField(stringResource(R.string.label_phone), profile.phone)
                LabelledField(
                    stringResource(R.string.label_notifications),
                    stringResource(if (profile.notificationsEnabled) R.string.notifications_on else R.string.notifications_off),
                )
            }
        }
    }
}

/*
 * Root of the admin screens: the admin menu and navigation between screens.
 *
 * Outstanding issues:
 * - Still need the way to navigate between entrant/owner/admin for testing.
 * - Browse images and the notification and message logs are not implemented yet.
 */
package com.example.campsiteapp.view

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.campsiteapp.data.CampsitesDatabase
import com.example.campsiteapp.R
import com.example.campsiteapp.controller.CampsiteAdminController
import com.example.campsiteapp.controller.ProfileAdminController
import com.example.campsiteapp.view.components.ScreenScaffold

private sealed interface Destination {
    data object Menu : Destination
    data object Campsites : Destination
    data object Profiles : Destination
    data class Profile(val deviceId: String) : Destination
}

/**
 * The admin UI, starting at the admin menu.
 *
 * @param campsitesDatabase where campsites are stored.
 */
@Composable
fun AdminApp(campsitesDatabase: CampsitesDatabase) {
    val campsiteAdminController = remember { CampsiteAdminController(campsitesDatabase) }
    val profileAdminController = remember { ProfileAdminController(campsitesDatabase) }
    var backStack by remember { mutableStateOf(listOf<Destination>(Destination.Menu)) }
    val navigate = { destination: Destination -> backStack = backStack + destination }
    val goBack = { backStack = backStack.dropLast(1) }
    val context = LocalContext.current
    val showRemoveFailed = { _: Exception ->
        Toast.makeText(context, R.string.remove_failed, Toast.LENGTH_SHORT).show()
    }

    BackHandler(enabled = backStack.size > 1, onBack = goBack)

    when (val destination = backStack.last()) {
        Destination.Menu -> AdminMenu(
            onBrowseCampsites = { navigate(Destination.Campsites) },
            onBrowseProfiles = { navigate(Destination.Profiles) },
        )

        Destination.Campsites -> AdminCampsiteListScreen(
            campsites = campsiteAdminController.campsites,
            onRemoveCampsite = { campsiteAdminController.removeCampsite(it.id, showRemoveFailed) },
            onBack = goBack,
        )

        Destination.Profiles -> AdminProfileListScreen(
            profiles = profileAdminController.profiles,
            onProfileClick = { navigate(Destination.Profile(it.deviceId)) },
            onBack = goBack,
        )

        is Destination.Profile -> {
            val profile = profileAdminController.profiles.find { it.deviceId == destination.deviceId }
            if (profile == null) {
                // The profile was removed on another device.
                LaunchedEffect(destination) { goBack() }
            } else {
                AdminProfileScreen(
                    profile = profile,
                    ownedCampsiteCount = profileAdminController.campsitesOwnedBy(profile.deviceId).size,
                    onRemove = {
                        profileAdminController.removeProfile(profile.deviceId, showRemoveFailed)
                        goBack()
                    },
                    onBack = goBack,
                )
            }
        }
    }
}

/**
 * Admin menu linking to the browse screens.
 */
@Composable
private fun AdminMenu(onBrowseCampsites: () -> Unit, onBrowseProfiles: () -> Unit) {
    ScreenScaffold(title = stringResource(R.string.admin_title), onBack = null) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            MenuItem(stringResource(R.string.browse_campsites), "browseCampsites", onBrowseCampsites)
            MenuItem(stringResource(R.string.browse_profiles), "browseProfiles", onBrowseProfiles)
        }
    }
}

@Composable
private fun MenuItem(text: String, testTag: String, onClick: () -> Unit) {
    Text(
        text,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
            .testTag(testTag),
    )
    HorizontalDivider()
}

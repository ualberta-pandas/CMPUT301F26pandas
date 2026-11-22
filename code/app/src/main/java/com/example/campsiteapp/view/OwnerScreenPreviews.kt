package com.example.campsiteapp.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.campsiteapp.controller.OwnerCampsiteController
import com.example.campsiteapp.model.Campsite

private val previewCampsites = listOf(
    Campsite(
        id = "1", name = "Elk Island Site", location = "Fort Saskatchewan",
        occupancy = 4.0, owner = "Sam", latitude = "53.5731", longitude = "-112.8584",
        price = "45", startDate = "2026/12/01", endDate = "2026/12/10",
        description = "Quiet site near a lake", tags = "lake,quiet",
        rules = "No loud music", status = "Available"
    ),
    Campsite(
        id = "2", name = "Cooking Lake Site", location = "Edmonton County",
        occupancy = 6.0, owner = "Sam", latitude = "53.4500", longitude = "-113.0300",
        price = "60", startDate = "2026/12/15", endDate = "2027/01/05",
        description = "Lakeside with fire pit", tags = "lake,fire pit",
        rules = "Quiet hours after 10pm", status = "Unavailable"
    )
)

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "Owner campsite list")
@Composable
private fun OwnerCampsiteListPreview() {
    OwnerCampsiteListScreen().CampsitesList(
        campsites = previewCampsites,
        onAddCampsite = {},
        onEditCampsite = { _, _ -> },
        deviceId = ""
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "Owner campsite list (empty)")
@Composable
private fun OwnerCampsiteListEmptyPreview() {
    OwnerCampsiteListScreen().CampsitesList(
        campsites = emptyList(),
        onAddCampsite = {},
        onEditCampsite = { _, _ -> },
        deviceId = ""
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, heightDp = 900, name = "Campsite details")
@Composable
private fun OwnerCampsiteDetailsPreview() {
    OwnerCampsiteDetailsScreen().CampsiteDetailsScreen(
        onDismissRequest = {},
        selectedCampsiteDetails = previewCampsites.first(),
        onEditCampsite = { _, _ -> }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, heightDp = 900, name = "Register campsite")
@Composable
private fun RegisterCampsitePreview() {
    OwnerCampsiteController().RegisterCampsite(
        onAddCampsite = {},
        onDismissRequest = {}
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, heightDp = 900, name = "Edit campsite")
@Composable
private fun EditCampsitePreview() {
    OwnerCampsiteController().EditCampsite(
        onDismissRequest = {},
        selectedCampsiteDetails = previewCampsites.first(),
        onEditCampsite = { _, _ -> }
    )
}
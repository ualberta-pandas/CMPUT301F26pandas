package com.example.campsiteapp.view

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.campsiteapp.controller.OwnerCampsiteController
import com.example.campsiteapp.data.DeviceIDs
import com.example.campsiteapp.model.Campsite

@RequiresApi(Build.VERSION_CODES.O)
class OwnerCampsiteListScreen {

    val ownerController = OwnerCampsiteController()

    @Composable
    fun CampsitesList(
        campsites: List<Campsite>,
        onAddCampsite: (Campsite) -> Unit,
        onEditCampsite: (Campsite, Campsite) -> Unit
    ) {

        val myCampsites = mutableListOf<Campsite>()

        val context = LocalContext.current
        val deviceId by produceState(initialValue = "") {
            value = DeviceIDs(context).fetchDeviceId() ?: ""
        }

        for (campsite in campsites){
            if (campsite.ownerId == deviceId){
                myCampsites.add(campsite)
            }

        }



        var showDetails by remember { mutableStateOf(false) }
        var selectedCampsite by remember { mutableStateOf<Campsite?>(null) }
        var showRegistration by remember { mutableStateOf(false) }

        Column {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
                    .testTag("campsiteList")
            ) {
                itemsIndexed(campsites) { index, campsite ->
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(
                        modifier = Modifier
                            .testTag("campsiteRow_$index")
                            .fillMaxWidth()
                            .padding(10.dp)
                            .border(
                                width = 2.dp,
                                shape = RoundedCornerShape(12.dp),
                                color = Color.Black
                            )
                            .padding(vertical = 20.dp)
                            .combinedClickable(
                                onDoubleClick = {},
                                onClick = {
                                    selectedCampsite = campsite
                                    showDetails = true
                                }
                            ),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Text(
                                text = campsite.name,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.testTag("campsiteName_$index")
                            )

                            Text(
                                text = campsite.location,
                                modifier = Modifier.testTag("campsiteLocation_$index")
                            )

                            Text(
                                text = campsite.occupancy.toString(),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.testTag("campsiteOccupancy_$index")
                            )

                            Text(
                                text = campsite.status,
                                modifier = Modifier
                                    .testTag("campsiteStatus_$index")
                                    .background(Color.Green)
                                    .border(
                                        width = 1.dp,
                                        color = Color.Green,
                                        shape = RoundedCornerShape(size = 20.dp)
                                    )
                            )

                            Text(
                                text = "# Slots available",
                                modifier = Modifier.testTag("campsiteSlots_$index")
                            )
                        }

                        Spacer(modifier = Modifier.width(80.dp))

                        val url = campsite.image
                        Column {
                            AsyncImage(
                                model = url,
                                contentDescription = "Campsite image",
                                modifier = Modifier
                                    .testTag("campsiteImage_$index")
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        if (index < campsites.lastIndex) {
                            HorizontalDivider()
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                FloatingActionButton(
                    onClick = { showRegistration = true },
                    modifier = Modifier.testTag("addCampsiteButton")
                ) { Text(text = "+", fontSize = 20.sp) }
            }

            if (showRegistration) {
                Dialog(
                    onDismissRequest = { showRegistration = false },
                    properties = DialogProperties(
                        usePlatformDefaultWidth = false
                    )
                ) {
                    Box(modifier = Modifier.testTag("registrationDialog")) {
                        ownerController.RegisterCampsite(
                            onDismissRequest = { showRegistration = false },
                            onAddCampsite = onAddCampsite
                        )
                    }
                }
            }

            if (showDetails && selectedCampsite != null) {
                Dialog(
                    onDismissRequest = { showDetails = false },
                    properties = DialogProperties(
                        usePlatformDefaultWidth = false
                    )
                ) {
                    OwnerCampsiteDetailsScreen().CampsiteDetailsScreen(
                        selectedCampsiteDetails = selectedCampsite,
                        onEditCampsite = onEditCampsite,
                        onDismissRequest = {
                            showDetails = false
                            selectedCampsite = null
                        }
                    )
                }
            }
        }
    }
}
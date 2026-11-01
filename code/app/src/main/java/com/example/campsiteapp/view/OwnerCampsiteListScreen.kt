package com.example.campsiteapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.campsiteapp.data.Campsite
import com.example.campsiteapp.controller.OwnerCampsiteController

class OwnerCampsiteListScreen {

    val ownerController = OwnerCampsiteController()

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CampsitesList(

        campsites: List<Campsite>,
        onAddCampsite: (Campsite) -> Unit

    ) {


        var showRegistration by remember { mutableStateOf(false) }


        Column() {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)

            ) {
                itemsIndexed(campsites) { index, campsite ->
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .border(
                                width = 2.dp,
                                shape = RoundedCornerShape(12.dp),
                                color = Color.Black
                            )
                            .padding(vertical = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Text(
                                text = campsite.name,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = campsite.location
                            )

                            Text(
                                text = campsite.occupancy,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Status",
                                modifier = Modifier
                                    .background(Color.Green)
                                    .border(
                                        width = 1.dp,
                                        color = Color.Green,
                                        shape = RoundedCornerShape(size = 20.dp)
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.width(80.dp))

                        val url = campsite.image
                        Column() {
                            AsyncImage(
                                model = url,
                                contentDescription = "Campsite image",
                                modifier = Modifier
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
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                FloatingActionButton(
                    onClick = {
                        showRegistration = true

                    }
                ) { Text(text = "+", fontSize = 20.sp) }
            }

            if (showRegistration) {
                Dialog(
                    onDismissRequest = { showRegistration = false },
                    properties = DialogProperties(
                        usePlatformDefaultWidth = false
                    )
                ) {
                   ownerController.RegisterCampsite(
                        onAddCampsite = onAddCampsite,
                        onDismissRequest = { showRegistration = false })
                }
            }

        }


    }

}
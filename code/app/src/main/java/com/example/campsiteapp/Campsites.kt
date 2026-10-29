package com.example.campsiteapp

import android.app.Dialog
import android.media.Image
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kotlin.contracts.contract


class Campsites {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CampsitesList(

        campsites: List<Campsite>,
        onAddCampsite: (Campsite) -> Unit

    ) {


        var showRegistration by remember { mutableStateOf(false) }
        var campName by remember { mutableStateOf("") }

        Column() {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
                    .weight(1f)

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
                                modifier = Modifier.background(Color.Green)
                                    .border(
                                        width = 1.dp,
                                        color = Color.Green,
                                        shape = RoundedCornerShape(size = 20.dp)
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.width(80.dp))

                        Column() {
                            Image(
                                painter = painterResource(
                                    id = R.drawable.ic_launcher_background
                                ),
                                contentDescription = "A cat",
                                modifier = Modifier.size(120.dp)
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

        }
                if (showRegistration) {
                    Dialog(
                        onDismissRequest = { showRegistration = false },
                        properties = DialogProperties(usePlatformDefaultWidth = false)
                    ) {
                        Card (
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize()

                                ) {

                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Add Campsite"
                                    )

                                    Spacer(modifier = Modifier.width(100.dp))

                                    TextButton(
                                        onClick = { showRegistration = false },
                                        modifier = Modifier.border(
                                            width = 1.dp,
                                            color = Color.Black,
                                            shape = RoundedCornerShape(50)
                                        )
                                            .size(30.dp)
                                    ) { Text("x") }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                TextField(
                                    value = campName,
                                    onValueChange = { campName = it },
                                    label = {
                                        Text("Campsite Name")
                                    },
                                    modifier = Modifier.testTag("campTextField")
                                        .border(
                                            width = 1.dp,
                                            color = Color.Black,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                //image uploading
                                var imageUri by remember { mutableStateOf<Uri?>(null) }

                                val imagePicker = rememberLauncherForActivityResult(
                                    contract = ActivityResultContracts.GetContent()
                                ) { uri ->
                                    imageUri = uri
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                        .border(
                                            width = 2.dp,
                                            color = Color.Gray,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .clickable {
                                            imagePicker.launch("image/*")
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (imageUri != null) {
                                        Image(
                                            painter = rememberAsyncImagePainter(imageUri),
                                            contentDescription = "Selected image",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.CloudUpload,
                                                contentDescription = "Upload image",
                                                modifier = Modifier.size(40.dp),
                                                tint = Color.Gray
                                            )

                                            Text("Tap to upload an image")
                                        }
                                    }
                                }

                                // other text fields and buttons

                                var location by remember { mutableStateOf("") }
                                var occupancy by remember { mutableStateOf("") }
                                var owner by remember { mutableStateOf("") }
                                var price by remember { mutableStateOf("") }
                                var mapLocation by remember { mutableStateOf("") }
                                var availableDates by remember { mutableStateOf("")}

//                                val datePickerState = rememberDatePickerState(
//                                    initialSelectedDateMillis = availableDates
//                                )

                                var description by remember { mutableStateOf("") }
                                var tags by remember { mutableStateOf("") }
                                var rules by remember { mutableStateOf("") }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row() {
                                    TextField(
                                        value = location,
                                        onValueChange = { location = it },
                                        label = {Text("Location")},
                                        modifier = Modifier.border(
                                            width = 1.dp,
                                    color = Color.Black,
                                    shape = RoundedCornerShape(16.dp)
                                    )

                                    )

                                    TextField(
                                        value = occupancy,
                                        onValueChange = { occupancy = it },
                                        label = {Text("Occupancy")},
                                        modifier = Modifier.border(
                                            width = 1.dp,
                                            color = Color.Black,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                    )

                                }


                                Spacer(modifier = Modifier.height(10.dp))
                                Row() {
                                    TextField(
                                        value = owner,
                                        onValueChange = { owner = it },
                                        label = {Text("Owner")},
                                        modifier = Modifier.border(
                                            width = 1.dp,
                                            color = Color.Black,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                    )

                                    TextField(
                                        value = price,
                                        onValueChange = { price = it },
                                        label = {Text("Price")},
                                        modifier = Modifier.border(
                                            width = 1.dp,
                                            color = Color.Black,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row() {

                                    TextField(
                                        value = availableDates,
                                        onValueChange = {availableDates = it},
                                        label = {Text("Available Dates")},
                                        modifier = Modifier.border(
                                            width = 1.dp,
                                            color = Color.Black,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                Row() {
                                    TextField(
                                        value = mapLocation,
                                        onValueChange = { mapLocation = it },
                                        label = {Text("Map Location")},
                                        modifier = Modifier.border(
                                            width = 1.dp,
                                            color = Color.Black,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                    )

                                    Button(
                                        onClick = {

                                        }
                                    ) { Text("Change")}
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                TextField(
                                    value = description,
                                    onValueChange = { description = it },
                                    label = {Text("Description")},
                                    maxLines = 4,
                                    modifier = Modifier.border(
                                        width = 1.dp,
                                        color = Color.Black,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                        .fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                TextField(
                                    value = tags,
                                    onValueChange = { tags = it },
                                    maxLines = 3,
                                    label = {Text("Tags")},
                                    modifier = Modifier.border(
                                        width = 1.dp,
                                        color = Color.Black,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                        .fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                TextField(
                                    value = rules,
                                    onValueChange = { rules = it },
                                    maxLines = 3,
                                    label = {Text("Rules and terms of use")},
                                    modifier = Modifier.border(
                                        width = 1.dp,
                                        color = Color.Black,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                        .fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                val context = LocalContext.current

                                Row(
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    Button(
                                        onClick = {
                                            if (campName.isNotBlank() && location.isNotBlank() &&
                                                occupancy.isNotBlank() && owner.isNotBlank() && price.isNotBlank()
                                                && mapLocation.isNotBlank() && description.isNotBlank() &&
                                                tags.isNotBlank() && rules.isNotBlank()) {

                                                onAddCampsite(
                                                    Campsite(
                                                        name = campName,
                                                        location = location,
                                                        occupancy = occupancy,
                                                        owner = owner,
                                                        price = price,
                                                        mapLocation = mapLocation,
                                                        adates = availableDates,
                                                        description = description,
                                                        tags = tags,
                                                        rules = rules

                                                    )
                                                )

                                                campName = ""
                                                location = ""
                                                occupancy = ""
                                                owner = ""
                                                mapLocation = ""
                                                price = ""
                                                description = ""
                                                tags = ""
                                                rules = ""

                                                Toast.makeText(context,"Campsite Added", Toast.LENGTH_LONG).show()

                                                showRegistration = false
                                            }

                                            else{
                                                Toast.makeText(context,"Fill all fields", Toast.LENGTH_LONG).show()
                                            }
                                        },



                                    ) { Text("Add") }
                                }


                            }
                        }
                    }
                }




                @Composable
                fun Lottery() {

                }

                @Composable
                fun Reservations() {

                }

            }
        }


package com.example.campsiteapp.view

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.campsiteapp.data.Campsite
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class OwnerCampsiteDetailsScreen {

    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable

    fun CampsiteDetailsScreen(
        onDismissRequest: () -> Unit,
        selectedCampsiteDetails: Campsite?

    ){
        Column(
            modifier = Modifier.fillMaxWidth()

        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()

            ) {
                Column(
                    modifier = Modifier.fillMaxSize()

                ) {


                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Top,

                        modifier = Modifier.fillMaxWidth()
                    ) {



                        TextButton(
                            onClick = onDismissRequest
                        ) { Text("x") }


                    }

                    Spacer(modifier = Modifier.height(10.dp))


                    var campName by remember { mutableStateOf("") }

                    TextField(
                        value = selectedCampsiteDetails?.name ?: "",
                        onValueChange = { campName = it },
                        label = {
                            Text("Campsite Name")
                        },
                        modifier = Modifier
                            .testTag("campName")
                            .border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            )
                            .fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Column() {
                        AsyncImage(
                            model = selectedCampsiteDetails?.image,
                            contentDescription = "Campsite image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )
                    }



                    // other text fields and buttons

                    var location by remember { mutableStateOf("") }
                    var occupancy by remember { mutableStateOf("") }
                    var owner by remember { mutableStateOf("") }
                    var price by remember { mutableStateOf("") }
                    var mapLocation by remember { mutableStateOf("") }
                    var availableDates by remember { mutableStateOf("") }


                    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")

                    val dateRangePickerState = rememberDateRangePickerState(
                      initialSelectedStartDate = LocalDate.parse(selectedCampsiteDetails?.startDate, formatter),
                        initialSelectedEndDate = LocalDate.parse(selectedCampsiteDetails?.endDate, formatter)

                    )


                    var description by remember { mutableStateOf("") }
                    var tags by remember { mutableStateOf("") }
                    var rules by remember { mutableStateOf("") }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row() {
                        TextField(
                            value = selectedCampsiteDetails?.location?:"",
                            onValueChange = { location = it },
                            label = { Text("Location") },
                            modifier = Modifier.border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            )

                        )

                        TextField(
                            value = selectedCampsiteDetails?.occupancy?:"",
                            onValueChange = { occupancy = it },
                            label = { Text("Occupancy") },
                            modifier = Modifier.border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            )
                        )

                    }


                    Spacer(modifier = Modifier.height(10.dp))
                    Row() {
                        TextField(
                            value = selectedCampsiteDetails?.owner?:"",
                            onValueChange = { owner = it },
                            label = { Text("Owner") },
                            modifier = Modifier.border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            )
                        )

                        TextField(
                            value = selectedCampsiteDetails?.price?:"",
                            onValueChange = { price = it },
                            label = { Text("Price") },
                            modifier = Modifier.border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    var showDatePicker by remember { mutableStateOf(false) }

                    Row() {

                        TextField(
                            value = selectedCampsiteDetails?.startDate+'-'+selectedCampsiteDetails?.endDate,
                            onValueChange = {availableDates = it},
                            label = { Text("Available Dates") },
                            modifier = Modifier.border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            ),
                            readOnly = true

                        )

                        Button(
                            onClick = {

                                showDatePicker = true

                            }
                        ) {
                            Text("View Dates")
                        }

                        if (showDatePicker) {
                            DatePickerDialog(
                                onDismissRequest = { showDatePicker = false },

                                confirmButton = {
                                    Button(
                                        onClick = {

                                            showDatePicker = false
                                        }
                                    ) { Text("Ok") }
                                },

                                dismissButton = {
                                    Button(
                                        onClick = {
                                            showDatePicker = false
                                        }
                                    ) {
                                        Text("Cancel")
                                    }
                                }
                            ) {

                                DateRangePicker(state = dateRangePickerState)
                            }
                        }


                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Row() {
                        TextField(
                            value = selectedCampsiteDetails?.mapLocation?:"",
                            onValueChange = { mapLocation = it },
                            label = { Text("Map Location") },
                            modifier = Modifier.border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            )
                        )

                        Button(
                            onClick = {

                            }
                        ) { Text("Change") }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    TextField(
                        value = selectedCampsiteDetails?.description?:"",
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        maxLines = 4,
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            )
                            .fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    TextField(
                        value = selectedCampsiteDetails?.tags?:"",
                        onValueChange = { tags = it },
                        maxLines = 3,
                        label = { Text("Tags") },
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            )
                            .fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    TextField(
                        value = selectedCampsiteDetails?.rules?:"",
                        onValueChange = { rules = it },
                        maxLines = 3,
                        label = { Text("Rules and terms of use") },
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
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


                            }

                        ) { Text( "Share") }

                        Button(
                            onClick = {


                            }

                        ) { Text( "Edit") }

                    }
                }

            }}
    }

}
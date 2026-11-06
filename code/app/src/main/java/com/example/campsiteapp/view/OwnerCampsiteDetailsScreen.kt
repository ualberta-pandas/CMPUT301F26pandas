package com.example.campsiteapp.view

/*
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.runtime.mutableDoubleStateOf
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.campsiteapp.controller.MapLocationPicker
import com.example.campsiteapp.controller.OwnerCampsiteController
import com.example.campsiteapp.model.Campsite
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


class OwnerCampsiteDetailsScreen {

    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable

    fun CampsiteDetailsScreen(
        onDismissRequest: () -> Unit,
        selectedCampsiteDetails: Campsite?,
        onEditCampsite: (Campsite, Campsite) -> Unit

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
                        verticalAlignment = Alignment.Top
                    ) {

                        TextButton(
                            onClick = onDismissRequest
                        ) { Text("x") }

                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                            Text("${selectedCampsiteDetails?.name} Details Screen")

                    }



                    Spacer(modifier = Modifier.height(10.dp))


                    var campName by remember(selectedCampsiteDetails) {
                        mutableStateOf(selectedCampsiteDetails?.name?:"") }

                    var longitude by remember(selectedCampsiteDetails) {
                        mutableDoubleStateOf(selectedCampsiteDetails?.latitude?.toDouble()
                            ?: 0.0) }
                    var latitude by remember(selectedCampsiteDetails) {
                        mutableDoubleStateOf(selectedCampsiteDetails?.longitude?.toDouble() ?:0.0) }



                    TextField(
                        value = campName,
                        onValueChange = { campName = it },
                        label = {
                            Text("Campsite Name")
                        },
                        modifier = Modifier
                            .testTag("campName")
                            .border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .fillMaxWidth(),
                        readOnly = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row() {

                        Column(
                            modifier = Modifier.width(200.dp).height(200.dp)
                        ) {
                            MapLocationPicker(
                                onLocationSelected = { lat, long ->
                                    latitude = lat
                                    longitude = long
                                }
                            )
                        }


                        Column() {
                            AsyncImage(
                                model = selectedCampsiteDetails?.image,
                                contentDescription = "Edit",
                                modifier = Modifier
                                    .width(200.dp)
                                    .height(200.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                    }



                    // other text fields and buttons

                    var location by remember(selectedCampsiteDetails) {
                        mutableStateOf(selectedCampsiteDetails?.location?:"") }
                    var occupancy by remember(selectedCampsiteDetails) {
                        mutableStateOf(selectedCampsiteDetails?.occupancy?:"") }
                    var owner by remember(selectedCampsiteDetails) {
                        mutableStateOf(selectedCampsiteDetails?.owner?:"") }
                    var price by remember (selectedCampsiteDetails){
                        mutableStateOf(selectedCampsiteDetails?.price?:"") }
                    var mapLocation by remember(selectedCampsiteDetails) {
                        mutableStateOf(selectedCampsiteDetails?.latitude+','+selectedCampsiteDetails?.longitude) }
                    var availableDates by remember(selectedCampsiteDetails) {
                        mutableStateOf(
                            selectedCampsiteDetails?.startDate+'-'+selectedCampsiteDetails?.endDate) }


                    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")

                    val startDate = runCatching {
                        selectedCampsiteDetails?.startDate?.let {
                            LocalDate.parse(it, formatter)
                        }
                    }.getOrNull()

                    val endDate = runCatching {
                        selectedCampsiteDetails?.endDate?.let {
                            LocalDate.parse(it, formatter)
                        }
                    }.getOrNull()

                    val dateRangePickerState = rememberDateRangePickerState(
                        initialSelectedStartDateMillis = startDate?.atStartOfDay(
                            ZoneOffset.UTC
                        )?.toInstant()?.toEpochMilli(),

                        initialSelectedEndDateMillis = endDate?.atStartOfDay(
                            ZoneOffset.UTC
                        )?.toInstant()?.toEpochMilli(),


                    )


                    var description by remember(selectedCampsiteDetails)
                    { mutableStateOf(selectedCampsiteDetails?.description?:"") }
                    var tags by remember(selectedCampsiteDetails) {
                        mutableStateOf(selectedCampsiteDetails?.tags?:"") }
                    var rules by remember(selectedCampsiteDetails) {
                        mutableStateOf(selectedCampsiteDetails?.rules?:"") }
                    var status by remember(selectedCampsiteDetails){
                        mutableStateOf(selectedCampsiteDetails?.status?:"")
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row() {
                        TextField(
                            value = location,
                            onValueChange = { location = it },
                            label = { Text("Location") },
                            modifier = Modifier.border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            ),
                            readOnly = true

                        )

                        TextField(
                            value = occupancy,
                            onValueChange = { occupancy = it },
                            label = { Text("Occupancy") },
                            modifier = Modifier.border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            ),
                            readOnly = true
                        )

                    }


                    Spacer(modifier = Modifier.height(10.dp))
                    Row() {
                        TextField(
                            value = owner,
                            onValueChange = { owner = it },
                            label = { Text("Owner") },
                            modifier = Modifier.border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            ),
                            readOnly = true
                        )

                        TextField(
                            value = price,
                            onValueChange = { price = it },
                            label = { Text("Price") },
                            modifier = Modifier.border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            ),
                            readOnly = true
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    var showDatePicker by remember { mutableStateOf(false) }

                    Row() {

                        TextField(
                            value = availableDates,
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

                    var showMap by remember { mutableStateOf(false) }








                    val coordinates = "$latitude,$longitude"

                    mapLocation = coordinates

                    Spacer(modifier = Modifier.height(10.dp))

                    TextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        maxLines = 4,
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            )
                            .fillMaxWidth(),
                        readOnly = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    TextField(
                        value = tags,
                        onValueChange = { tags = it },
                        maxLines = 3,
                        label = { Text("Tags") },
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            )
                            .fillMaxWidth(),
                        readOnly = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    TextField(
                        value = rules,
                        onValueChange = { rules = it },
                        maxLines = 3,
                        label = { Text("Rules and terms of use") },
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            )
                            .fillMaxWidth(),
                        readOnly = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    TextField(
                        value = status,
                        onValueChange = {status = it},
                        label = { Text("Status") },
                        modifier = Modifier.border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                        ),
                        readOnly = true

                    )


                    Spacer(modifier = Modifier.height(10.dp))

                    val context = LocalContext.current
                    val qrScanScreen = remember { QrScanScreen() }
                    var showQrScanScreen by remember { mutableStateOf(false) }

                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Button(
                            onClick = {

                              showQrScanScreen = true
                            }

                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = Color.White
                            )
                        }


                        if (showQrScanScreen && selectedCampsiteDetails != null){

                            Dialog(
                                onDismissRequest = {showQrScanScreen = false},
                                properties = DialogProperties(
                                    usePlatformDefaultWidth = false
                                )

                            ) {

                                Card(

                                ) {
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        qrScanScreen.QrScan(
                                            campsiteId = selectedCampsiteDetails.id
                                        )
                                    }
                                }
                            }


                        }

                        var showEdit by remember { mutableStateOf(false) }
                        Button(
                            onClick = {

                                showEdit = true

                            }

                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        if (showEdit && selectedCampsiteDetails != null){
                            Dialog(
                                onDismissRequest = {showEdit = false
                                                   },
                                properties = DialogProperties(
                                    usePlatformDefaultWidth = false
                                )

                            ) {

                                Card(

                                ) {
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {

                                        OwnerCampsiteController().EditCampsite(
                                            onDismissRequest = { showEdit = false },
                                            selectedCampsiteDetails,
                                            onEditCampsite = onEditCampsite
                                        )
                                    }
                                }
                            }
                        }

                    }
                }

            }}
    }

}

 */
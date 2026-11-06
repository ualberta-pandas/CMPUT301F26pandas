package com.example.campsiteapp.controller

import android.content.Context
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import coil.compose.rememberAsyncImagePainter
import com.example.campsiteapp.data.Campsite
import com.example.campsiteapp.view.QrScanScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.UUID
import android.location.Geocoder
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
class OwnerCampsiteController (

) {


    @Composable
    fun RegisterCampsite(
        onAddCampsite: (Campsite) -> Unit,
        onDismissRequest: () -> Unit

    ) {



        val scope = rememberCoroutineScope()
        var isUploading by remember { mutableStateOf(false) }
        val imageUploadService = remember {
            ImageUploadService()
        }
        var imageUri by remember { mutableStateOf<Uri?>(null) }

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
                        horizontalArrangement = Arrangement.Center,

                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Add Campsite"
                        )



                        TextButton(
                            onClick = onDismissRequest
                        ) { Text("x") }


                    }

                    Spacer(modifier = Modifier.height(10.dp))


                    var campName by remember { mutableStateOf("") }

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
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            )
                            .fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))


                    //image uploading

                    imageUploadService.ImagePicker(
                        imageUri = imageUri,
                        onImageSelected = {
                            uri ->
                            imageUri = uri
                        }
                    )


                    // other text fields and buttons

                    var location by remember { mutableStateOf("") }
                    var occupancy by remember { mutableStateOf("") }
                    var owner by remember { mutableStateOf("") }
                    var price by remember { mutableStateOf("") }
                    var mapLocation by remember { mutableStateOf("") }
                    var availableDates by remember { mutableStateOf("") }


                    val dateRangePickerState = rememberDateRangePickerState()


                    var description by remember { mutableStateOf("") }
                    var tags by remember { mutableStateOf("") }
                    var rules by remember { mutableStateOf("") }
                    var status by remember{mutableStateOf("Available")}

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
                            )

                        )

                        TextField(
                            value = occupancy,
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
                            value = owner,
                            onValueChange = { owner = it },
                            label = { Text("Owner") },
                            modifier = Modifier.border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            )
                        )

                        TextField(
                            value = price,
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
                    var startDate by remember { mutableStateOf("") }
                    var endDate by remember { mutableStateOf("") }
                    Row() {

                        TextField(
                            value = availableDates,
                            onValueChange = {},
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
                            Text("Select Dates")
                        }

                        if (showDatePicker) {
                            DatePickerDialog(
                                onDismissRequest = { showDatePicker = false },

                                confirmButton = {
                                    Button(
                                        onClick = {

                                            val start = dateRangePickerState.selectedStartDateMillis
                                            val end = dateRangePickerState.selectedEndDateMillis

                                            if (start != null && end != null) {
                                                val formatter = SimpleDateFormat(
                                                    "dd/MM/yyyy",
                                                    Locale.getDefault()
                                                )

                                                availableDates =
                                                    "${formatter.format(Date(start))} - " +
                                                            formatter.format(Date(end))

                                                startDate = "${formatter.format(Date(start))}"
                                                endDate = "${formatter.format(Date(end))}"



                                            }


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


                    var showMap by remember { mutableStateOf(false) }

                    var longitude by remember { mutableDoubleStateOf(0.0) }
                    var latitude by remember { mutableDoubleStateOf(0.0) }


                    if (showMap) {

                        MapLocationPicker(
                            onLocationSelected = { lat, long ->
                                latitude = lat
                                longitude = long
                                showMap = false
                            }
                        )

                    }



                    val coordinates = "$latitude,$longitude"

                    mapLocation = coordinates

                    Spacer(modifier = Modifier.height(10.dp))
                    Row() {
                        TextField(
                            value = mapLocation,
                            onValueChange = { mapLocation = it },
                            label = { Text("Map Location") },
                            modifier = Modifier.border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(16.dp)
                            )
                        )

                        Button(
                            onClick = {
                                showMap = true
                            }
                        ) { Text("Choose Location") }
                    }

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
                            .fillMaxWidth()
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
                            .fillMaxWidth()
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
                            .fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    TextField(
                        value = status,
                        onValueChange = { status = it },
                        label = { Text("Status") },
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

                                if (campName.isBlank() || location.isBlank() ||
                                    occupancy.isBlank() || owner.isBlank() ||
                                    price.isBlank() || mapLocation.isBlank() ||
                                    description.isBlank() || tags.isBlank() ||
                                    rules.isBlank() || price.isBlank() || status.isBlank()
                                ) {
                                    Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT)
                                        .show()
                                    return@Button
                                }



                                scope.launch {
                                    isUploading = true

                                    try {

                                        val selectedImage = imageUri

                                        if (selectedImage == null) {
                                            Toast.makeText(
                                                context,
                                                "Please select an image",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            return@launch
                                        }

                                        val imageUrl = imageUploadService.uploadImage(
                                            context,
                                            selectedImage
                                        )

                                        onAddCampsite(
                                            Campsite(
                                                name = campName,
                                                location = location,
                                                occupancy = occupancy,
                                                owner = owner,
                                                price = price,
                                                latitude = latitude.toString(),
                                                longitude = longitude.toString(),
                                                startDate = startDate,
                                                endDate = endDate,
                                                description = description,
                                                tags = tags,
                                                rules = rules,
                                                image = imageUrl,
                                                status = status
                                            )
                                        )

                                        Toast.makeText(
                                            context,
                                            "Campsite added!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        onDismissRequest()

                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            context,
                                            "Upload failed: ${e.message}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } finally {
                                        isUploading = false
                                    }
                                }
                            }

                        ) { Text(if (isUploading) "Uploading..." else "Add") }

                    }
                }
            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    @Composable

    fun EditCampsite(
        onDismissRequest: () -> Unit,
        selectedCampsiteDetails: Campsite?,
        onEditCampsite: (Campsite, Campsite) -> Unit

    ){

        val scope = rememberCoroutineScope()
        var isUploading by remember { mutableStateOf(false) }
        val imageUploadService = remember {
            ImageUploadService()
        }
        val originalImageUrl = selectedCampsiteDetails?.image ?: ""

        var imageUri by remember(selectedCampsiteDetails) {
            mutableStateOf(
                originalImageUrl.let { Uri.parse(it) }
            )
        }



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
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            )
                            .fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Column() {
                        imageUploadService.ImagePicker(
                            imageUri = imageUri,
                            onImageSelected = {
                                    uri ->
                                imageUri = uri
                            }
                        )
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
                        mutableStateOf("${selectedCampsiteDetails?.latitude}, ${selectedCampsiteDetails?.longitude}") }
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
                            java.time.ZoneOffset.UTC
                        )?.toInstant()?.toEpochMilli(),

                        initialSelectedEndDateMillis = endDate?.atStartOfDay(
                            java.time.ZoneOffset.UTC
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
                            )

                        )

                        TextField(
                            value = occupancy,
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
                            value = owner,
                            onValueChange = { owner = it },
                            label = { Text("Owner") },
                            modifier = Modifier.border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            )
                        )

                        TextField(
                            value = price,
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
                            value = availableDates,
                            onValueChange = {availableDates = it},
                            label = { Text("Available Dates") },
                            modifier = Modifier.border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            )

                        )

                        Button(
                            onClick = {

                                showDatePicker = true

                            }
                        ) {
                            Text("Change Dates")
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

                    var longitude by remember(selectedCampsiteDetails) {
                        mutableDoubleStateOf(selectedCampsiteDetails?.latitude?.toDouble()
                        ?: 0.0) }
                    var latitude by remember(selectedCampsiteDetails) {
                        mutableDoubleStateOf(selectedCampsiteDetails?.longitude?.toDouble() ?:0.0) }


                    if (showMap) {

                        MapLocationPicker(
                            onLocationSelected = { lat, long ->
                                latitude = lat
                                longitude = long
                                showMap = false
                            }
                        )

                    }



                    val coordinates = "$latitude,$longitude"

                    mapLocation = coordinates

                    Spacer(modifier = Modifier.height(10.dp))
                    Row() {
                        TextField(
                            value = mapLocation,
                            onValueChange = { mapLocation = it },
                            label = { Text("Map Location") },
                            modifier = Modifier.border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(16.dp)
                            )
                        )

                        Button(
                            onClick = {
                                showMap = true
                            }
                        ) { Text("Choose Location") }
                    }

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
                            .fillMaxWidth()
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
                            .fillMaxWidth()
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
                            .fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    TextField(
                        value = status,
                        onValueChange = { status = it },
                        label = { Text("Status") },
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

                    val context = LocalContext.current
                    val qrScanScreen = remember { QrScanScreen() }
                    var showQrScanScreen by remember { mutableStateOf(false) }

                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Bottom
                    ) {

                        Button(
                            onClick = {

                                if (campName.isBlank() || location.isBlank() ||
                                    occupancy.isBlank() || owner.isBlank() ||
                                    price.isBlank() || mapLocation.isBlank() ||
                                    description.isBlank() || tags.isBlank() ||
                                    rules.isBlank() || status.isBlank()
                                ) {
                                    Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT)
                                        .show()
                                    return@Button
                                }

                                scope.launch {
                                    isUploading = true

                                    try {

                                        val imageUrl = if (imageUri.toString() == originalImageUrl) {
                                            originalImageUrl
                                        } else {
                                            imageUploadService.uploadImage(context, imageUri!!)
                                        }

                                       onEditCampsite(
                                           Campsite(
                                               selectedCampsiteDetails?.id ?: "",
                                               selectedCampsiteDetails?.name ?: "",
                                               selectedCampsiteDetails?.location ?: "",
                                               selectedCampsiteDetails?.occupancy ?: "",
                                               selectedCampsiteDetails?.owner ?: "",
                                               selectedCampsiteDetails?.latitude ?: "",
                                               selectedCampsiteDetails?.longitude?:"",
                                               selectedCampsiteDetails?.price ?: "",
                                               selectedCampsiteDetails?.startDate ?: "",
                                               selectedCampsiteDetails?.endDate ?: "",
                                               selectedCampsiteDetails?.description ?: "",
                                               selectedCampsiteDetails?.tags ?: "",
                                               selectedCampsiteDetails?.rules ?: "",
                                               selectedCampsiteDetails?.image ?: "",
                                               selectedCampsiteDetails?.status?:""


                                           ),
                                           Campsite(
                                               name = campName,
                                               location = location,
                                               occupancy = occupancy,
                                               owner = owner,
                                               latitude = latitude.toString(),
                                               longitude = longitude.toString(),
                                               price = price,
                                               startDate = startDate.toString(),
                                               endDate = endDate.toString(),
                                               description = description,
                                               tags = tags,
                                               rules = rules,
                                               image = imageUrl,
                                               status = status

                                           )
                                       )

                                        Toast.makeText(context, "Successfully Edited", Toast.LENGTH_LONG).show()
                                        onDismissRequest()



                                } catch (e: Exception) {
                                Toast.makeText(
                                    context,
                                    "Upload failed: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            } finally {
                                isUploading = false
                            }
                                }



                            }

                        ) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = "Update",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }

                    }
                }

            }}
    }
}

*/
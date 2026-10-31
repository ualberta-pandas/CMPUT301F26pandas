package com.example.campsiteapp

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.io.DataOutputStream
import java.util.UUID



class Campsite {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CampsitesList(

        campsites: List<CampsiteObject>,
        onAddCampsite: (CampsiteObject) -> Unit

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

            if (showRegistration){
                Dialog(
                    onDismissRequest = {showRegistration= false},
                    properties = DialogProperties(
                        usePlatformDefaultWidth = false
                    )
                ){
                    RegisterCampsite(onAddCampsite = onAddCampsite, onDismissRequest= { showRegistration = false })
                }
            }

        }


    }

    private suspend fun uploadImage(
        context: android.content.Context,
        imageUri: Uri
    ): String = withContext(Dispatchers.IO) {

        val cloudName = "lgwzmfuq"
        val uploadPreset = "campsite_upload"

        val mimeType = context.contentResolver.getType(imageUri) ?: "image/jpeg"

        val imageBytes = context.contentResolver
            .openInputStream(imageUri)
            ?.use { it.readBytes() }
            ?: throw Exception("Cannot read image")

        val boundary = "Boundary-${UUID.randomUUID()}"

        val connection = URL(
            "https://api.cloudinary.com/v1_1/$cloudName/image/upload"
        ).openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.setRequestProperty(
                "Content-Type",
                "multipart/form-data; boundary=$boundary"
            )

            DataOutputStream(connection.outputStream).use { output ->

                output.writeBytes("--$boundary\r\n")
                output.writeBytes(
                    "Content-Disposition: form-data; name=\"upload_preset\"\r\n\r\n"
                )
                output.writeBytes("$uploadPreset\r\n")

                output.writeBytes("--$boundary\r\n")
                output.writeBytes(
                    "Content-Disposition: form-data; name=\"file\"; filename=\"image\"\r\n"
                )
                output.writeBytes("Content-Type: $mimeType\r\n\r\n")
                output.write(imageBytes)
                output.writeBytes("\r\n")
                output.writeBytes("--$boundary--\r\n")
            }

            val code = connection.responseCode

            val response = if (code in 200..299) {
                connection.inputStream.bufferedReader().use { it.readText() }
            } else {
                connection.errorStream?.bufferedReader()?.use { it.readText() }
                    ?: "Unknown error"
            }

            if (code !in 200..299) {
                throw Exception(response)
            }

            JSONObject(response).getString("secure_url")

        } finally {
            connection.disconnect()
        }
    }
    @Composable
    fun RegisterCampsite(
        onAddCampsite: (CampsiteObject) -> Unit,
        onDismissRequest: () -> Unit

    ) {

        val scope = rememberCoroutineScope()
        var isUploading by remember { mutableStateOf(false) }

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
                                shape = RoundedCornerShape(16.dp)
                            )
                            .fillMaxWidth()
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


                               val dateRangePickerState = rememberDateRangePickerState()


                    var description by remember { mutableStateOf("") }
                    var tags by remember { mutableStateOf("") }
                    var rules by remember { mutableStateOf("") }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row() {
                        TextField(
                            value = location,
                            onValueChange = { location = it },
                            label = { Text("Location") },
                            modifier = Modifier.border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(16.dp)
                            )

                        )

                        TextField(
                            value = occupancy,
                            onValueChange = { occupancy = it },
                            label = { Text("Occupancy") },
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
                            label = { Text("Owner") },
                            modifier = Modifier.border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(16.dp)
                            )
                        )

                        TextField(
                            value = price,
                            onValueChange = { price = it },
                            label = { Text("Price") },
                            modifier = Modifier.border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(16.dp)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    var showDatePicker by remember { mutableStateOf(false) }
                    Row() {

                        TextField(
                            value = availableDates,
                            onValueChange = {},
                            label = { Text("Available Dates") },
                            modifier = Modifier.border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(16.dp)
                            ),
                            readOnly = true

                        )

                        Button(
                            onClick = {

                                showDatePicker = true

                            }
                        ){
                           Text("Select Dates")
                        }

                        if (showDatePicker){
                            DatePickerDialog(
                                onDismissRequest = {showDatePicker = false},

                                confirmButton = {
                                    Button(
                                        onClick = {

                                            val start = dateRangePickerState.selectedStartDateMillis
                                            val end = dateRangePickerState.selectedEndDateMillis

                                            if(start != null && end != null){
                                                val formatter = java.text.SimpleDateFormat(
                                                    "dd/MM/yyyy",
                                                    java.util.Locale.getDefault()
                                                )

                                                availableDates =
                                                    "${formatter.format(java.util.Date(start))} - "+
                                                            formatter.format(java.util.Date(end))

                                            }


                                            showDatePicker = false
                                        }
                                    ){Text("Ok")}
                                },

                                dismissButton = {
                                    Button(
                                        onClick = {
                                            showDatePicker = false
                                        }
                                    ){
                                        Text("Cancel")
                                    }
                                }
                            ){

                                DateRangePicker(state = dateRangePickerState)
                            }
                        }


                    }

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

                            }
                        ) { Text("Change") }
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
                                shape = RoundedCornerShape(16.dp)
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
                                shape = RoundedCornerShape(16.dp)
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

                                if (campName.isBlank() || location.isBlank() ||
                                    occupancy.isBlank() || owner.isBlank() ||
                                    price.isBlank() || mapLocation.isBlank() ||
                                    description.isBlank() || tags.isBlank() ||
                                    rules.isBlank()
                                ) {
                                    Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                if (imageUri == null) {
                                    Toast.makeText(context, "Select an image", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                scope.launch {
                                    isUploading = true

                                    try {
                                        val imageUrl = uploadImage(context, imageUri!!)

                                        onAddCampsite(
                                            CampsiteObject(
                                                name = campName,
                                                location = location,
                                                occupancy = occupancy,
                                                owner = owner,
                                                price = price,
                                                mapLocation = mapLocation,
                                                adates = availableDates,
                                                description = description,
                                                tags = tags,
                                                rules = rules,
                                                image = imageUrl
                                            )
                                        )

                                        Toast.makeText(context, "Campsite added!", Toast.LENGTH_SHORT).show()
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


            @Composable
            fun Lottery() {

            }

            @Composable
            fun Reservations() {

            }


        }


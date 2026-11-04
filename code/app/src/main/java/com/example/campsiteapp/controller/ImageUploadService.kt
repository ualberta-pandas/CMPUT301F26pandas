
package com.example.campsiteapp.controller

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID

class ImageUploadService {

    // Holds the selected image
    var imageUri by mutableStateOf<Uri?>(null)


    // Image picker UI
    @Composable
    fun ImagePicker(
        modifier: Modifier = Modifier,
        imageUri: Uri?,
        onImageSelected: (Uri?) -> Unit
    ) {

        val context = LocalContext.current
        val imagePicker = rememberLauncherForActivityResult(

            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->

            if (uri != null) {
                android.util.Log.d("IMAGE_TEST", "URI: $uri")

                try {
                    val stream = context.contentResolver.openInputStream(uri)

                    if (stream != null) {
                        android.util.Log.d("IMAGE_TEST", "IMAGE READ SUCCESS")
                        stream.close()
                    } else {
                        android.util.Log.e("IMAGE_TEST", "IMAGE STREAM IS NULL")
                    }

                } catch (e: Exception) {
                    android.util.Log.e("IMAGE_TEST", "IMAGE READ FAILED", e)
                }

                onImageSelected(uri)
            } else{
                android.util.Log.d("IMAGE_TEST", "UPLOAD BOX CLICKED")
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .border(2.dp, Color.Gray, RoundedCornerShape(12.dp))
                .clickable {
                    android.util.Log.d("IMAGE_TEST", "UPLOAD BOX CLICKED")
                    imagePicker.launch(
                        PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    ))
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
    }

    // Upload selected image to Cloudinary
    suspend fun uploadImage(
        context: Context,
        uri: Uri
    ): String = withContext(Dispatchers.IO) {

        val cloudName = "lgwzmfuq"
        val uploadPreset = "campsite_upload"

        val mimeType =
            context.contentResolver.getType(uri) ?: "image/jpeg"

        val imageBytes = context.contentResolver
            .openInputStream(uri)
            ?.use { it.readBytes() }
            ?: throw Exception("Cannot read image")

        val boundary = "Boundary-${UUID.randomUUID()}"

        val connection = URL(
            "https://api.cloudinary.com/v1_1/$cloudName/image/upload"
        ).openConnection() as HttpURLConnection

        try {

            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.connectTimeout = 15000
            connection.readTimeout = 15000

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
                connection.inputStream.bufferedReader().use {
                    it.readText()
                }
            } else {
                connection.errorStream?.bufferedReader()?.use {
                    it.readText()
                } ?: "Unknown error"
            }

            if (code !in 200..299) {
                throw Exception("Cloudinary error: $response")
            }

            JSONObject(response).getString("secure_url")

        } finally {
            connection.disconnect()
        }
    }

    // Clear selected image
    fun clearImage() {
        imageUri = null
    }
}

package com.example.campsiteapp.controller

import android.annotation.SuppressLint
import android.location.Geocoder
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@SuppressLint("DefaultLocale")
@Composable
fun MapLocationPicker(
    onLocationSelected: (Double, Double) -> Unit
) {
    val context = LocalContext.current

    val initialLocation = LatLng(53.5461, -113.4938)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLocation, 12f)
    }

    val markerState = rememberMarkerState(
        position = initialLocation
    )

    var address by remember {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        GoogleMap(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),

            cameraPositionState = cameraPositionState,

            onMapClick = { location ->
                markerState.position = location
            }
        ) {
            Marker(
                state = markerState,
                draggable = true
            )
        }

        Button(
            onClick = {
                scope.launch {
                    val latitude = String.format("%.2f",markerState.position.latitude).toDouble()
                    val longitude =  String.format("%.2f",markerState.position.latitude).toDouble()

                    onLocationSelected(latitude, longitude)

                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirm Location")
        }
    }
}
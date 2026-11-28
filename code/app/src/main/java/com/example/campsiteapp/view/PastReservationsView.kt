/*
 * Screen listing an entrant's past reservations, including cancelled, rejected
 * and expired ones.
 *
 * Outstanding issues:
 * - Rating a past reservation (US 01.13.01) is not implemented yet.
 */
package com.example.campsiteapp.view

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.campsiteapp.controller.ReservationController
import com.example.campsiteapp.controller.saveRating
import com.example.campsiteapp.data.DeviceIDs
import com.example.campsiteapp.model.Campsite
import com.example.campsiteapp.model.Reservation
import com.example.campsiteapp.model.ReservationStatus
import com.example.campsiteapp.ui.theme.CampsiteAppTheme
import com.example.campsiteapp.view.components.ScreenScaffold
import kotlinx.coroutines.launch

/**
 * Activity that shows [PastReservationsView].
 */
class PastReservationsScreen : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var reservationController by mutableStateOf<ReservationController?>(null)
        lifecycleScope.launch {
            val deviceIds = DeviceIDs(applicationContext)
            val deviceId = deviceIds.fetchDeviceId() ?: deviceIds.registerDeviceId()
            reservationController = ReservationController(deviceId)
        }
        setContent {
            CampsiteAppTheme {
                PastReservationsView(
                    reservationController?.pastReservations.orEmpty(),
                    onBack = ::finish,
                    deviceID = deviceId.toString()
                )
            }
        }
    }
}

/**
 * List of past reservations.
 *
 * @param reservations reservations to show.
 * @param onBack called when Back is pressed.
 */
@Composable
fun PastReservationsView(reservations: List<Reservation>, onBack: () -> Unit, deviceID: String) {
    ScreenScaffold(title = "Your past reservations", onBack = onBack) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(reservations, key = { it.id }) { reservation ->
                ReservationCard(reservation, deviceID)
            }
        }
    }
}

@Composable
private fun ReservationCard(reservation: Reservation, deviceID: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Text(reservation.campsite.name, fontSize = 18.sp)
        Text(reservation.campsite.address, fontSize = 12.sp, color = Color.Gray)
        Text("Camped ${reservation.startDate} - ${reservation.endDate}", fontSize = 12.sp)
        Text(
            reservation.status.displayText,
            fontSize = 12.sp,
            color = if (reservation.status == ReservationStatus.COMPLETED) Color.Gray else Color.Red,
        )
        var showRating by remember { mutableStateOf(false) }

        var rating by remember { mutableIntStateOf(0) }



        Button(
            onClick = {
                showRating = true
            }
        ) { Text("Rate campsite") }

        if (showRating){

            StarRating(
                rating = rating,
                onRatingChange = { number ->
                    rating = number
                    println("User picked $number")
                }
            )


            Button(onClick = {
                saveRating(reservation.campsite.id, deviceId = deviceID, rating)
                showRating = false}) {
                Text("Submit")
            }



        }
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun PastReservationsViewPreview() {
//    val campsite = Campsite(name = "Red Deer Park", address = "1302 100, Red Deer, AB")
//    CampsiteAppTheme {
//        PastReservationsView(
//            reservations = listOf(
//                Reservation("1", campsite, startDate = "01/08/2026", endDate = "04/08/2026"),
//                Reservation(
//                    "2",
//                    campsite.copy(name = "Dry Area Campsite", address = "123 Dry Forest st, Calgary, AB"),
//                    startDate = "13/05/2026",
//                    endDate = "15/05/2026",
//                    status = ReservationStatus.CANCELLED,
//                ),
//            ),
//            onBack = {},
//            deviceID = DeviceIDs(@).fetchDeviceId()?:""
//        )
//    }
//}

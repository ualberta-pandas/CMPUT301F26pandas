package com.example.campsiteapp.view

import android.R
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.campsiteapp.controller.OwnerCampsiteController
import com.example.campsiteapp.data.DeviceIDs
import com.example.campsiteapp.model.Reservation
import com.example.campsiteapp.model.Campsite
import com.example.campsiteapp.model.ReservationDatabase
import com.example.campsiteapp.model.ReservationStatus

class OwnerReservationsList {

   @Composable
   fun ReservationsList(
       activeReservations: List<Reservation>,
       pasReservations: List<Reservation>
   ){
       
           var selectedReservation by remember { mutableStateOf<Reservation?>(null) }

       Text("Active Reservations", modifier = Modifier.padding(start = 16.dp))

       Column {
               LazyColumn(
                   modifier = Modifier
                       .fillMaxWidth()
                       .weight(0.8f)
                       .testTag("reservationsList")
               ) {
                   itemsIndexed(activeReservations) { index, reservation ->
                       Spacer(modifier = Modifier.height(30.dp))
                       Row(
                           modifier = Modifier
                               .testTag("reservationRow_$index")
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
                                       selectedReservation = reservation

                                   }
                               ),
                           horizontalArrangement = Arrangement.spacedBy(16.dp)
                       ) {

                           var showReject by remember { mutableStateOf(false) }
                           Column(
                               modifier = Modifier
                                   .fillMaxWidth()
                                   .padding(horizontal = 16.dp)
                                   .border(1.dp, Color.LightGray, RoundedCornerShape(14.dp))
                                   .padding(horizontal = 16.dp, vertical = 12.dp),
                           ) {
                               Text(reservation.campsite.name, fontSize = 18.sp)
                               Text("Booked from ${reservation.startDate} to ${reservation.endDate}", fontSize = 12.sp)
                               Text("Entrant name: ${reservation.entrantId}")
                               Button(
                                   onClick = {showReject = true},
                                   modifier = Modifier.background(color = Color.Red)

                               ) { Text("x", color = Color.White)}
                           }

                           if (showReject){
                               Dialog(
                                   onDismissRequest = {
                                       showReject = false
                                   },
                                   properties = DialogProperties(
                                       usePlatformDefaultWidth = false
                                   )
                               ) {

                                   Box(modifier = Modifier.padding(24.dp)){
                                       Text("Reject reservation")
                                       Text("Are you sure you wanna reject reservation?")
                                       Text(reservation.campsite.name, fontSize = 18.sp)
                                       Text("Booked from ${reservation.startDate} to ${reservation.endDate}", fontSize = 12.sp)
                                       Text("Entrant name: ${reservation.entrantId}")

                                       Row() {
                                           Button(
                                               onClick = {showReject = true},
                                               modifier = Modifier.background(color = Color.Gray)
                                           ) { Text("Reject", color = Color.White) }

                                           Button(
                                               onClick = {
                                                   ReservationDatabase().deleteReservation(reservation.id)
                                                   showReject = false
                                               },
                                               modifier = Modifier.background(color = Color.Red)
                                           ) { Text("Reject", color = Color.White) }
                                       }
                                   }

                               }
                           }


                           if (index < activeReservations.lastIndex) {
                               HorizontalDivider()
                           }
                       }
                   }
               }

               Spacer(modifier = Modifier.weight(1f))

               Text("Past Reservations", modifier = Modifier.padding(start = 16.dp))


               Column {
                   LazyColumn(
                       modifier = Modifier
                           .fillMaxWidth()
                           .weight(0.8f)
                           .testTag("reservationsList")
                   ) {
                       itemsIndexed(pasReservations) { index, reservation ->
                           Spacer(modifier = Modifier.height(30.dp))
                           Row(
                               modifier = Modifier
                                   .testTag("reservationRow_$index")
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
                                           selectedReservation = reservation

                                       }
                                   ),
                               horizontalArrangement = Arrangement.spacedBy(16.dp)
                           ) {


                               ReservationCard(reservation)


                               if (index < pasReservations.lastIndex) {
                                   HorizontalDivider()
                               }
                           }
                       }
                   }


               }


               }
           }
       }


@Composable
private fun ReservationCard(reservation:Reservation) {
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
    }
}
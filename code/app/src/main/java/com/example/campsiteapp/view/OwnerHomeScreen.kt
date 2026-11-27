package com.example.campsiteapp.view


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.campsiteapp.controller.LotteryController
import com.example.campsiteapp.controller.OwnerCampsiteController
import com.example.campsiteapp.data.CampsitesDatabase
import com.example.campsiteapp.data.DeviceIDs
import com.example.campsiteapp.data.LotteryDatabase
import com.example.campsiteapp.model.Campsite
import com.example.campsiteapp.view.OwnerCampsiteListScreen
import com.example.campsiteapp.view.LotteryOwnerScreen
import com.example.campsiteapp.model.Lottery
import com.example.campsiteapp.model.ReservationDatabase
//import com.example.campsiteapp.controller.OwnerReservationsController


@RequiresApi(Build.VERSION_CODES.O)
@Composable

fun OwnerHomeScreen(
    campsitesDatabase: CampsitesDatabase,
    lotteryDatabase: LotteryDatabase,
    reservationDatabase: ReservationDatabase,
    onBack: () -> Unit,
    deviceId: String? = null
) {
    val tabs = listOf("Campsites", "Lottery", "Reservations")
    val lotteryController = remember { LotteryController(lotteryDatabase) }
    var selectedIndex by remember { mutableIntStateOf(0) }

    val context = LocalContext.current
    var lookupCount by remember { mutableIntStateOf(0) }
    val lookedUpId by produceState<String?>(initialValue = null, deviceId, lookupCount) {
        value = deviceId ?: (DeviceIDs(context).fetchDeviceId() ?: "")
    }
    val ownerId = deviceId ?: lookedUpId

    val myCampsites = if(ownerId.isNullOrEmpty()) {
        emptyList()
    } else {
        campsitesDatabase.campsites.filter { it.ownerId == ownerId }
    }

    val myLotteries = if(ownerId.isNullOrEmpty()) {
        emptyList()
    } else {
        lotteryDatabase.lotteries.filter { it.ownerId == ownerId }
    }

    val myReservations = if(ownerId.isNullOrEmpty()) {
//        emptyList()
    } else {
//        reservationDatabase.reservations.filter { it.ownerId == ownerId }
    }



    Column(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = onBack
            ) { Text("<-") }
            Text(text = "My Campsites")
        }

        SecondaryTabRow(
            selectedTabIndex = selectedIndex,
            containerColor = Color.Transparent,
            divider = {},
            indicator = {
                Box(
                    Modifier
                        .tabIndicatorOffset(selectedIndex)
                        .padding(horizontal = 12.dp)
                        .height(3.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFF25402F))
                )
            }
        ) {
            tabs.forEachIndexed { index, label ->
                Tab(
                    selected = index == selectedIndex,
                    onClick = { selectedIndex = index },
                    text = { Text(label, fontWeight = FontWeight.SemiBold) },
                    selectedContentColor = MaterialTheme.colorScheme.onSurface,
                    unselectedContentColor =
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }



        val ownerCampsiteController = remember { OwnerCampsiteController() }
//        val ownerReservationsController = remember { OwnerReservationsController() }

        when (selectedIndex) {
            0 -> OwnerCampsiteListScreen().CampsitesList(
                campsites = myCampsites,
                onAddCampsite = {
                    campsitesDatabase.addCampsite(it)
                    lookupCount++
                },
                onEditCampsite = { old, new ->
                    campsitesDatabase.updateCampsite(old, new)

                })
            // Owners only see and run lotteries for their own campsites.
            1 -> LotteryOwnerScreen().LotteryList(
                lotteries = myLotteries,
                campsites = myCampsites,
                onCreateLottery = { lotteryController.createLottery(it.copy(ownerId = ownerId ?: "")) },
                onUpdateLottery = { lotteryController.updateLottery(it) }
            )

//            2 -> OwnerReservationsList().ReservationsList(
//                activeReservations = ownerReservationsController.getReservationsById(ownerId),
//                pasReservations = ownerReservationsController.getPastReservationsById(ownerId),
//                cancelledReservations = ownerReservationsController.getCancelledReservationsById(ownerId)
//            )
        }
    }
}







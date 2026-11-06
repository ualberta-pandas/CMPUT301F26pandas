package com.example.campsiteapp.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.campsiteapp.helper.formatDate
import com.example.campsiteapp.model.Campsite
import com.example.campsiteapp.model.Lottery
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


/**
 * Owner's view of lotteries
 *
 * Lists every lottery with the campsite it is for, its waitlist and
 * booking periods, number of winners, waitlist capacity, and status.
 * Does not handle creating and editing
 */
class LotteryOwnerScreen {

    @Composable
    fun LotteryList(
        lotteries: List<Lottery>,
        campsites: List<Campsite>,
        onCreateClick: () -> Unit,
        onLotteryClick: (Lottery) -> Unit
    ) {
        val campsiteNames = campsites.associate { it.id to it.name }

        Box(modifier = Modifier.fillMaxSize()) {
            if(lotteries.isEmpty()) {
                Text(
                    text = "No lotteries yet",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .testTag("no-lotteries")
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(lotteries) { lottery ->
                        LotteryCard(lottery, campsiteNames) { onLotteryClick(lottery) }
                    }
                }
            }

            FloatingActionButton(
                onClick = onCreateClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .testTag("createLotteryButton")
            ) {
                Text("+")
            }
        }
    }
}

@Composable
private fun LotteryCard(
    lottery: Lottery,
    campsiteNames: Map<String, String>,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("lotteryCard")
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                lottery.campsiteIds.joinToString { campsiteNames[it] ?: "Unknown campsite" },
                fontWeight = FontWeight.Bold
            )
            Text("Registration: ${formatDate(lottery.lotteryStartDate)} - ${formatDate(lottery.lotteryEndDate)}")
            Text("Booking: ${formatDate(lottery.bookingStartDate)} - ${formatDate(lottery.bookingEndDate)}")
            Text("Winners: ${lottery.numWinners}")
            Text("Waitlist: ${lottery.waitlistCapacity?.toString() ?: "No limit"}")
            if(lottery.status.isNotBlank()) {
                Text("Status: ${lottery.status}")
            }
        }
    }
}
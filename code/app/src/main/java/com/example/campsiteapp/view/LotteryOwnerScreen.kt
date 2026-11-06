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
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.campsiteapp.model.Lottery


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
        onCreateLottery: (Lottery) -> String?,
        onUpdateLottery: (Lottery) -> String?
    ) {
        val campsiteNames = campsites.associate { it.id to it.name }
        var creating by remember { mutableStateOf(false) }
        var editing by remember { mutableStateOf<Lottery?>(null) }

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
                        LotteryCard(lottery, campsiteNames) { editing = lottery }
                    }
                }
            }

            FloatingActionButton(
                onClick = { creating = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .testTag("createLotteryButton")
            ) {
                Text("+")
            }
        }

        if(creating || editing != null) {
            val lotteryBeingEdited = editing
            val close = { creating = false; editing = null }

            Dialog(
                onDismissRequest = close,
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                LotteryForm(
                    initial = lotteryBeingEdited,
                    campsites = campsites,
                    onSave = { lottery ->
                        if(lotteryBeingEdited == null) {
                            onCreateLottery(lottery)
                        } else {
                            onUpdateLottery(lottery)
                        }
                    },
                    onClose = close
                )
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
            Text("Registration: ${lottery.lotteryStartDate} - ${lottery.lotteryEndDate}")
            Text("Booking: ${lottery.bookingStartDate} - ${lottery.bookingEndDate}")
            Text("Winners: ${lottery.numWinners}")
            Text("Waitlist: ${lottery.waitlistMax?.toString() ?: "No limit"}")
            if(lottery.status.isNotBlank()) {
                Text("Status: ${lottery.status}")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LotteryListPreview() {
    LotteryOwnerScreen().LotteryList(
        lotteries = listOf(
            Lottery(
                id = "1",
                campsiteIds = listOf("a", "b"),
                lotteryStartDate = "2026-11-10",
                lotteryEndDate = "2026-11-20",
                bookingStartDate = "2026-12-01",
                bookingEndDate = "2026-12-05",
                numWinners = 2,
                waitlistMax = 10,
                status = "Registration open"
            ),
            Lottery(
                id = "2",
                campsiteIds = listOf("a"),
                lotteryStartDate = "2026-12-01",
                lotteryEndDate = "2026-12-10",
                bookingStartDate = "2027-01-05",
                bookingEndDate = "2027-01-09",
                numWinners = 1
            )
        ),
        campsites = listOf(
            Campsite(id = "a", name = "Elk Island Site"),
            Campsite(id = "b", name = "Cooking Lake Site")
        ),
        onCreateLottery = { null },
        onUpdateLottery = { null }
    )
}
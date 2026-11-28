package com.example.campsiteapp.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.IntentCompat
import coil.compose.AsyncImage
import com.example.campsiteapp.controller.EntrantCampsiteController
import com.example.campsiteapp.controller.EntrantCampsiteController.toggleSearchResultsScreen
import com.example.campsiteapp.model.Campsite
import com.example.campsiteapp.model.Lottery
import com.example.campsiteapp.ui.theme.CampsiteAppTheme

import com.example.campsiteapp.controller.EntrantWaitlistController.addEntrantToWaitlist
import com.example.campsiteapp.controller.EntrantWaitlistController.verifyUserOnWaitlist
import com.example.campsiteapp.controller.EntrantWaitlistController.removeEntrantFromWaitlist

import com.example.campsiteapp.data.DeviceIDs
import com.example.campsiteapp.view.components.CampsiteDetailsCard
import com.example.campsiteapp.view.components.EntrantScreenHeader

private val pill = RoundedCornerShape(50)

class CampsiteDetailsScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // TODO: get these from your controllers / intent
        val campsite = IntentCompat.getSerializableExtra(intent, "campsite", Campsite::class.java)
        val lottery = IntentCompat.getSerializableExtra(intent, "lottery", Lottery::class.java)

        if (campsite == null || lottery == null) {
            finish()
            return
        }

        setContent {
            val context = LocalContext.current
            val deviceId by produceState(initialValue = "") {
                value = DeviceIDs(context).fetchDeviceId() ?: ""
            }

            CampsiteAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (deviceId.isNotEmpty()) CampsiteView(
                        campsiteSelection = campsite,
                        lottery = lottery,
                        userOnWaitlist = {
                            verifyUserOnWaitlist(lottery, deviceId)
                        },
                        waitlistSelectionClick = { userOnWaitList ->
                            if (!userOnWaitList) {
                                addEntrantToWaitlist(lottery, deviceId)
                            } else {
                                removeEntrantFromWaitlist(lottery, deviceId)
                            }
                        },

                        updateByUserSearch = {search ->
                            toggleSearchResultsScreen(this@CampsiteDetailsScreen, search)
                        },

                        onBack = { finish() },
                        context = this@CampsiteDetailsScreen,

                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun CampsiteView(
    campsiteSelection: Campsite,
    lottery: Lottery,

    userOnWaitlist: () -> Boolean,
    waitlistSelectionClick: (userOnWaitlist: Boolean) -> Unit,
    onBack: () -> Unit,

    updateByUserSearch: (search: Triple<String, String, String>) -> Unit,
    context: Activity,

    modifier: Modifier = Modifier
) {

    var onWaitlist by remember { mutableStateOf(userOnWaitlist()) }

    Column(
        modifier = modifier.fillMaxSize()
            .padding(top = 5.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(5.dp))

        EntrantScreenHeader(
            context = context,
            modifier = Modifier,
            updateByUserSearch = updateByUserSearch
        )

        CampsiteDetailsCard(
            modifier = Modifier.weight(1f),
            lottery = lottery,
            ExitHandler = onBack
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Available starting ${campsiteSelection.startDate}",
                modifier = Modifier
                    .border(1.dp, Color.Black, pill)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )


            Button(
                onClick = { waitlistSelectionClick(onWaitlist); onWaitlist = !onWaitlist },
                enabled = onWaitlist || !lottery.isFull(),
                shape = pill,
                colors = if (onWaitlist) {
                    ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFC0392B),
                        contentColor = Color.White
                    )
                } else {
                    ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFB5C7BE),
                        contentColor = Color(0xFF2F5240)
                    )
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(if (onWaitlist) "Leave Waitlist" else "Join Waitlist", fontSize = 20.sp)
            }
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun CampsiteDetailsScreenPreview() {
    val campsite = Campsite(
        id = "1",
        name = "Campsite Location A",
        location = "123 Jasper, Edmonton, AB",
        occupancy = 8.0,
        owner = "dana_device_id",
        latitude = "53.5461",
        longitude = "-113.4938",
        price = "25.00",
        startDate = "2026-09-22",
        endDate = "2026-10-30",
        description = "Open campsite near the lake.\n\nAmenities\n• Running water on site\n• Grill on site\n• Great views",
        tags = "lake,quiet",
        rules = "No loud music after 10pm",
        image = "",
        status = "open",
        rating = 4.4,
        address = "6702 280 Bays Drive"
    )

    val lottery = Lottery(
        campsiteId = 1,
        numWinners = 5,
        lotteryStartDate = "2026-09-22",
        lotteryEndDate = "2026-10-30",
        waitlistMax = 50,
        waitlist = arrayListOf("abc123", "xyz789"),
        campsite = campsite
    )

    CampsiteAppTheme {
        CampsiteView(
            campsiteSelection = campsite,
            lottery = lottery,
            userOnWaitlist = {true},
            waitlistSelectionClick = {},
            onBack = {},
            updateByUserSearch = {}
        )
    }
}

 */
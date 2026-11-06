package com.example.campsiteapp.view

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.IntentCompat
import com.example.campsiteapp.controller.EntrantCampsiteController.toggleSearchResultsScreen
import com.example.campsiteapp.model.Campsite
import com.example.campsiteapp.model.Lottery
import com.example.campsiteapp.ui.theme.CampsiteAppTheme

import com.example.campsiteapp.controller.EntrantWaitlistController.addEntrantToWaitlist
import com.example.campsiteapp.controller.EntrantWaitlistController.verifyUserOnWaitlist
import com.example.campsiteapp.controller.EntrantWaitlistController.removeEntrantFromWaitlist

import com.example.campsiteapp.data.DeviceIDs
private val pill = RoundedCornerShape(50)

class CampsiteDetailsScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // TODO: get these from your controllers / intent
        val campsite = IntentCompat.getSerializableExtra(intent, "campsite", Campsite::class.java)!!
        val lottery = IntentCompat.getSerializableExtra(intent, "lottery", Lottery::class.java)

        setContent {
            val context = LocalContext.current
            val deviceId by produceState(initialValue = "") {
                value = DeviceIDs(context).fetchDeviceId() ?: ""
            }


            CampsiteAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CampsiteView(
                        campsiteSelection = campsite,
                        lottery = lottery,
                        userOnWaitlist = {
                            verifyUserOnWaitlist(lottery, deviceId)
                        },

                        waitlistSelectionClick = { userOnWaitList ->
                            if (userOnWaitList) {
                                addEntrantToWaitlist(lottery, deviceId)
                            } else {
                                removeEntrantFromWaitlist(lottery, deviceId)
                            }
                        },

                        updateByUserSearch = {search ->
                            toggleSearchResultsScreen(this@CampsiteDetailsScreen, search)
                        },

                        onBack = { finish() },
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
    lottery: Lottery?,

    userOnWaitlist: () -> Boolean,
    waitlistSelectionClick: (userOnWaitlist: Boolean) -> Unit,
    onBack: () -> Unit,

    updateByUserSearch: (search: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var entrantSearch by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(5.dp))

        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu"
            )
        }

        BasicTextField(
            value = entrantSearch,
            onValueChange = { entrantSearch = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { updateByUserSearch(entrantSearch) }),
            textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .border(1.dp, Color.LightGray, pill)
                .padding(start = 16.dp, end = 12.dp, top = 8.dp, bottom = 8.dp),
            decorationBox = { innerTextField ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (entrantSearch.isEmpty()) {
                            Text("search campsites", color = Color.Gray, fontSize = 14.sp)
                        }
                        innerTextField()
                    }
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {


            lottery?.let { // if the lottery is not nan then show this
                Text(
                    text = "${lottery.lotteryStartDate} - ${lottery.lotteryEndDate}",
                    modifier = Modifier
                        .border(1.dp, Color.Black, pill)
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            lottery?.let {
                Text(
                    text = "Guest Count: ${campsiteSelection.occupancy.toInt()}",
                    modifier = Modifier
                        .border(1.dp, Color.Black, pill)
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    fontSize = 12.sp
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Text(
                "< back to results",
                fontSize = 10.sp,
                modifier = Modifier
                    .clickable { onBack() }
                    .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
                    .size(width = 100.dp, height = 30.dp)
            )
        }

        Text(
            text = """
                ${campsiteSelection.name}
                ${campsiteSelection.location}
                ${campsiteSelection.occupancy} people maximum occupancy
                ${campsiteSelection.rating}/5 ★
            """.trimIndent(),
            fontSize = 12.sp,

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .background(Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = campsiteSelection.name, fontSize = 22.sp)

            Text(
                text = """
                
                
                IMAGE GOES HERE 
                
                
    
            """.trimIndent()
            ) // TODO: promo image

            Spacer(Modifier.height(10.dp))

            Text(text = campsiteSelection.description)
        }

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

            if (lottery != null) {
                Button(
                    onClick = { waitlistSelectionClick(userOnWaitlist()) },
                    enabled = userOnWaitlist() || !lottery.isFull(),
                    shape = pill,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFB5C7BE),
                        contentColor = Color(0xFF2F5240)
                    ),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(if (userOnWaitlist()) "Leave Waitlist" else "Join Waitlist", fontSize = 20.sp)
                }
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
        campsite = campsite,
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
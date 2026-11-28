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
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
import com.example.campsiteapp.model.Quad
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

                        openProfileScreen = { EntrantCampsiteController.toggleProfileScreen(this@CampsiteDetailsScreen) },
                        openAdminScreen = { EntrantCampsiteController.toggleAdminScreen(this@CampsiteDetailsScreen) },
                        openOwnerScreen = { EntrantCampsiteController.toggleOwnerScreen(this@CampsiteDetailsScreen) },
                        openMenuScreen = {EntrantCampsiteController.toggleMenuScreen(this@CampsiteDetailsScreen)},
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

    updateByUserSearch: (search: Quad<String, String, String, String>) -> Unit,

    openProfileScreen: () -> Unit,
    openOwnerScreen: () -> Unit,
    openAdminScreen: () -> Unit,
    openMenuScreen: () -> Unit,

    modifier: Modifier = Modifier
) {
    var entrantSearch by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var availableDates by remember { mutableStateOf("Select Dates") }
    var guestCount by remember { mutableIntStateOf(0) }
    val filters = Quad(entrantSearch,location,availableDates,guestCount.toString())
    var showDatePicker by remember { mutableStateOf(false) }
    var showGuests by remember { mutableStateOf(false) }
    var menuOpen by remember { mutableStateOf(false) }
    var onWaitlist by remember { mutableStateOf(userOnWaitlist()) }

    Column(
        modifier = modifier.fillMaxSize()
            .padding(top = 5.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box {
                IconButton(onClick = { menuOpen = true }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu"
                    )
                }

                DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                    DropdownMenuItem(
                        text = { Text("Admin") },
                        onClick = {
                            menuOpen = false
                            openAdminScreen()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Owner") },
                        onClick = {
                            menuOpen = false
                            openOwnerScreen()
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Main Menu") },
                        onClick = {
                            menuOpen = false
                            openMenuScreen()
                        }
                    )
                }
            }


            IconButton(
                modifier = Modifier.background(Color(0xFFE8DEF8), CircleShape),
                onClick = {
                    openProfileScreen()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color(0xFF4F378B)
                )
            }



            BasicTextField(
                value = entrantSearch,
                onValueChange = { entrantSearch = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { updateByUserSearch(filters) }),
                textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                modifier = Modifier
                    .weight(1f)
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
                                .clickable(
                                    onClick = {updateByUserSearch(filters)}
                                )
                        )
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = availableDates,
                modifier = Modifier
                    .border(1.dp, Color.Black, pill)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .clickable(
                        onClick = { showDatePicker = true }
                    )
            )


            val dateRangePickerState = rememberDateRangePickerState()
            var startDate by remember { mutableStateOf("") }
            var endDate by remember { mutableStateOf("") }

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },

                    confirmButton = {
                        Button(
                            onClick = {

                                val start = dateRangePickerState.selectedStartDateMillis
                                val end = dateRangePickerState.selectedEndDateMillis

                                if (start != null && end != null) {
                                    val formatter = SimpleDateFormat(
                                        "dd/MM/yyyy",
                                        Locale.getDefault()
                                    )

                                    availableDates =
                                        "${formatter.format(Date(start))} - " +
                                                formatter.format(Date(end))

                                    startDate = "${formatter.format(Date(start))}"
                                    endDate = "${formatter.format(Date(end))}"


                                }


                                showDatePicker = false
                            }
                        ) { Text("Ok") }
                    },

                    dismissButton = {
                        Button(
                            onClick = {
                                showDatePicker = false
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                ) {

                    DateRangePicker(state = dateRangePickerState)
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = "Guest Count: $guestCount",
                modifier = Modifier
                    .border(1.dp, Color.Black, pill)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .clickable(
                        onClick = { showGuests = true }
                    )
            )
            TextField(
                value = location,
                onValueChange = {location = it},
                label = { Text("Location") }
            )

            if (showGuests) {
                Dialog(

                    onDismissRequest = { showGuests = false },
                    properties = DialogProperties(
                        usePlatformDefaultWidth = false
                    )
                ) {
                    Surface(shape = RoundedCornerShape(16.dp)) {
                        Box(modifier = Modifier.padding(26.dp)) {

                            CounterView(
                                count = guestCount,
                                onCountChange = { guestCount = it },
                                min = 1,
                                max = 100
                            )
                        }

                    }
                }
            }

        }



        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {


            lottery.let { // if the lottery is not nan then show this
                Text(
                    text = "${lottery.lotteryStartDate} - ${lottery.lotteryEndDate}",
                    modifier = Modifier
                        .border(1.dp, Color.Black, pill)
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            lottery.let {
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

        val averageRating = if (campsiteSelection.ratingCount > 0) {
            "%.1f".format(campsiteSelection.ratingSum.toDouble() / campsiteSelection.ratingCount)
        } else {
            "0.0"
        }

        Text(
            text = """
                ${campsiteSelection.name}
                ${campsiteSelection.location}
                ${campsiteSelection.occupancy} people maximum occupancy
                $averageRating/5 ★
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

            AsyncImage(
                model = campsiteSelection.image,
                contentDescription = "Campsite image",
                modifier = Modifier
                    .testTag("campsiteImage")
                    .width(200.dp)
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

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
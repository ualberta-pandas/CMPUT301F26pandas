package com.example.campsiteapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.campsiteapp.ui.theme.CampsiteAppTheme

import com.example.campsiteapp.model.Lottery

import com.example.campsiteapp.controller.EntrantCampsiteController

import com.example.campsiteapp.controller.EntrantCampsiteController.toggleSearchResultsScreen
import com.example.campsiteapp.controller.EntrantCampsiteController.toggleCampsiteDetailsScreen
import com.example.campsiteapp.model.Quad
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

private val pill = RoundedCornerShape(50)

class CampsiteListScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // TODO: get these from your controllers /

        val searchResult = Quad(
            intent.getStringExtra("search") ?: "",
            intent.getStringExtra("location") ?: "",
            intent.getStringExtra("dates") ?: "",
            intent.getStringExtra("guests")?:""
        )

        setContent {
            CampsiteAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CampsiteListView(
                        modifier = Modifier.padding(innerPadding),
                        updateByUserSearch = { newSearch ->
                            toggleSearchResultsScreen(this@CampsiteListScreen, newSearch)
                            finish()
                        },
                        onLotteryClick = {lotteryId ->
                            toggleCampsiteDetailsScreen(this@CampsiteListScreen, lotteryId)
                            finish()
                        },

                        lotteries = EntrantCampsiteController.getResultsBySearch(searchResult), // Know that campsites is based on the lottery database
                        openProfileScreen = { EntrantCampsiteController.toggleProfileScreen(this@CampsiteListScreen) },
                        openAdminScreen = { EntrantCampsiteController.toggleAdminScreen(this@CampsiteListScreen) },
                        openOwnerScreen = { EntrantCampsiteController.toggleOwnerScreen(this@CampsiteListScreen) },
                        openMenuScreen = {EntrantCampsiteController.toggleMenuScreen(this@CampsiteListScreen)},
                    )

                }
            }
        }
    }
}




@Composable
fun CampsiteListView(
    modifier: Modifier = Modifier,
    updateByUserSearch: (search: Quad<String, String, String, String>) -> Unit,
    onLotteryClick: (String) -> Unit,

    openProfileScreen: () -> Unit, // confirm if the user should be able to open their profile screen from here
    openOwnerScreen: () -> Unit,
    openAdminScreen: () -> Unit,
    openMenuScreen: () -> Unit,

    lotteries: List<Lottery>,
) {
    var entrantSearch by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var availableDates by remember { mutableStateOf("Select Dates") }
    var guestCount by remember { mutableIntStateOf(0) }
    val filters = Quad(entrantSearch,location,availableDates,guestCount.toString())
    var menuOpen by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showGuests by remember { mutableStateOf(false) }

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

        Spacer(modifier = Modifier.height(15.dp))

        Row() {
            TextField(
                value = location,
                onValueChange = {location = it},
                label = { Text("Location") }
            )

        }

        val openIfOpen: (Lottery) -> Unit = { lottery ->
            if (lottery.lotteryEndDate >= LocalDate.now().toString()) {
                onLotteryClick(lottery.id)
            }
        }

        DisplayCampsitesAsCards(
            Modifier, lotteries = lotteries,
            openIfOpen,
            getTextAttributes = { lottery ->
                val currSize = lottery.waitlistMax - lottery.waitlist.size

                var localDate: String = LocalDate.now().toString()
                if (lottery.lotteryEndDate < LocalDate.now().toString()) {
                    Pair("Lottery is closed", Color(0xFFD9B8B8))
                } else if (currSize == 0) {
                    Pair("Closed - No Space Left", Color(0xFFD9B8B8))
                } else if (currSize < 2) {
                    Pair("Open - 2 Spaces remaining", Color(0xFFE6DDB0))
                } else {
                    Pair("Open - Many Spots", Color(0xFFB5C7BE))
                }

            })

        Spacer(modifier = Modifier.height(12.dp))
    }
}

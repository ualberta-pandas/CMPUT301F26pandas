package com.example.campsiteapp.view

import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campsiteapp.controller.EntrantCampsiteController
import com.example.campsiteapp.controller.EntrantOfferController
import com.example.campsiteapp.model.Campsite
import com.example.campsiteapp.data.DeviceIDs
import com.example.campsiteapp.model.Lottery
import com.example.campsiteapp.ui.theme.CampsiteAppTheme
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.campsiteapp.controller.ReservationController
import com.example.campsiteapp.data.CampsitesDatabase
import com.example.campsiteapp.model.Quad
import com.example.campsiteapp.model.Reservation

private val pill = RoundedCornerShape(50)

class EntrantHomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CampsiteAppTheme {
                val context = LocalContext.current
                val deviceId by produceState(initialValue = "") {
                    value = DeviceIDs(context).fetchDeviceId() ?: ""
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    EntrantHomeScreenView(
                        modifier = Modifier.padding(innerPadding),
                        updateByUserSearch = { search ->
                            EntrantCampsiteController.toggleSearchResultsScreen(this@EntrantHomeScreen, search)
                        },
                        deviceId = deviceId,

                        openProfileScreen = { EntrantCampsiteController.toggleProfileScreen(this@EntrantHomeScreen) },
                        openAdminScreen = { EntrantCampsiteController.toggleAdminScreen(this@EntrantHomeScreen) },
                        openOwnerScreen = { EntrantCampsiteController.toggleOwnerScreen(this@EntrantHomeScreen) },
                        openEntrantOfferScreen = {lottery, hasOffer ->
                            EntrantOfferController.toggleOfferScreen(this@EntrantHomeScreen, lottery, hasOffer)
                        },

                        openReservationScreen = {
                            ReservationController(entrantId = deviceId).togglePastReservationsScreen(this@EntrantHomeScreen)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EntrantHomeScreenView(
    modifier: Modifier = Modifier,
    updateByUserSearch: (Quad<String, String, String, String>) -> Unit,
    deviceId: String,

    openProfileScreen: () -> Unit,
    openOwnerScreen: () -> Unit,
    openAdminScreen: () -> Unit,
    openEntrantOfferScreen: (lottery: Lottery, hasOffer: Boolean) -> Unit,
    openReservationScreen: () -> Unit
) {
    var entrantSearch by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var availableDates by remember { mutableStateOf("Select Dates") }
    var guestCount by remember { mutableIntStateOf(0) }
    val filters = Quad(entrantSearch,location,availableDates,guestCount.toString())
    var menuOpen by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showGuests by remember { mutableStateOf(false) }

    Column(modifier = modifier
        .fillMaxSize()
        .padding(top = 5.dp)) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
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
            }

            BasicTextField(
                value = entrantSearch,
                onValueChange = { entrantSearch = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { updateByUserSearch(filters) }),
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
                            modifier = Modifier
                                .size(20.dp)
                                .clickable(
                                    onClick = { updateByUserSearch(filters) }
                                )
                        )
                    }
                }
            )

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


            Spacer(modifier = Modifier.height(64.dp))

            val reservations = EntrantCampsiteController.getReservationsById(deviceId)
            val lotteries = EntrantCampsiteController.getLotteriesById(deviceId)
            Log.d("HOME", "deviceId=$deviceId lotteries=${lotteries.size}")


            Text("Your Confirmed Bookings", modifier = Modifier.padding(start = 16.dp))
            DisplayBookingsAsCards(
                modifier = Modifier.fillMaxWidth(),
                reservations = reservations,
                onReservationEvent = openReservationScreen
            )

            Spacer(modifier = Modifier.height(32.dp))
            Text("Your Pending Bookings", modifier = Modifier.padding(start = 16.dp))

            DisplayCampsitesAsCards(
                modifier = Modifier.fillMaxWidth(),
                lotteries = lotteries,
                onClickEvent = { lottery ->
                    val hasOffer: Boolean = deviceId in lottery.winners
                    openEntrantOfferScreen(lottery, hasOffer)
                },
                getTextAttributes = { lottery ->
                    if (deviceId in lottery.winners) {
                        Pair(
                            "Campsite won - respond before ${lottery.bookingEndDate}",
                            Color(0xFFE6DDB0)
                        )
                    } else if (lottery.lotteryEndDate <= LocalDate.now().toString()) {
                        Pair("Entered in lottery", Color(0xFFD9D9D9))
                    } else {
                        Pair("Entered in lottery", Color(0xFFD9D9D9))
                    }
                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = "See past reservations",
                modifier = Modifier
                    .border(1.dp, Color.Black, pill)
                    .clickable { openReservationScreen() }
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )



            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = "Messages"
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "privacy policy",
                color = Color(0xFF909090),
                modifier = Modifier.clickable {}
            )

            Text(
                text = "terms & conditions",
                color = Color(0xFF909090),
                modifier = Modifier.clickable {}
            )
        }
    }
}



@Composable
fun CounterView(
    count: Int,
    onCountChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    min: Int = 0,
    max: Int = 20
) {
    Row(
        modifier = modifier
            .border(1.dp, Color.Black, pill)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { if (count > min) onCountChange(count - 1) },
            enabled = count > min
        ) {
            Icon(Icons.Default.Remove, contentDescription = "Less")
        }

        Text(text = "$count", fontSize = 16.sp)

        IconButton(
            onClick = { if (count < max) onCountChange(count + 1) },
            enabled = count < max
        ) {
            Icon(Icons.Default.Add, contentDescription = "More")
        }
    }
}
@Composable
fun DisplayBookingsAsCards(
    modifier: Modifier = Modifier,
    reservations: List<Reservation>,
    onReservationEvent: () -> Unit,
){
    /*
        the firestore document doesn't keep the serialized object, so
        reservationDB needs to fetch the campsite using the campsiteId as a column
    */


    val campsitesDb = remember { CampsitesDatabase() }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,

        ){
        reservations.forEach { reservation ->
            val campsite = campsitesDb.campsites
                .find { it.id == reservation.campsite.id } ?: reservation.campsite
            Box(
                modifier = Modifier
                    .size(width = 350.dp, height = 170.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(14.dp))
                    .clickable {
                        // TODO
                        onReservationEvent()
                    }
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
                ) {
                    Text(text = campsite.name, fontSize = 18.sp)
                    Text(text = campsite.address, fontSize = 12.sp, color = Color.Gray)

                    val averageRating = if (campsite.ratingCount > 0) {
                        "%.1f".format(campsite.ratingSum.toDouble() / campsite.ratingCount)
                    } else {
                        "0.0"
                    }

                    Text(
                        text = "Maximum ${campsite.occupancy} occupancy - $averageRating stars",
                        fontSize = 12.sp
                    )
                    Text(text = "Available from ${campsite.startDate}", fontSize = 12.sp)

                    val status: Pair<String, Color> = Pair(
                        "Booked - Cancel Before ${campsite.startDate}",
                        Color(0xFFB5C7BE)
                    )
                    Text(
                        text = status.first,
                        color = Color.Black,
                        modifier = Modifier
                            .width(300.dp)
                            .height(32.dp)
                            .background(status.second, RoundedCornerShape(50))
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }

    }

}
@Composable
fun DisplayCampsitesAsCards(modifier: Modifier = Modifier,
                            lotteries: List<Lottery>, onClickEvent: (Lottery) -> Unit,
                            getTextAttributes: (Lottery) -> Pair<String, Color>,
){

    val campsitesDb = remember { CampsitesDatabase() }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        lotteries.forEach { lottery ->
            val campsite = campsitesDb.campsites
                .find { it.id == lottery.campsite.id } ?: lottery.campsite
            val status: Pair<String, Color> = getTextAttributes(lottery)

            Box(modifier = Modifier
                .width(350.dp)
                .heightIn(min = 170.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(14.dp))
                .clickable {
                    onClickEvent(lottery)
                }
            ){
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
                ){
                    Text(text = campsite.name, fontSize = 18.sp)
                    Text(text = campsite.address, fontSize = 12.sp, color = Color.Gray)

                    val averageRating = if(campsite.ratingCount>0){
                        "%.1f".format(campsite.ratingSum.toDouble()/campsite.ratingCount)
                    }else{
                        "0.0"
                    }


                    Text(text = "Maximum ${campsite.occupancy} occupancy - $averageRating stars",
                        fontSize = 12.sp)
                    Text(text = "Available from ${campsite.startDate}", fontSize = 12.sp)

                    Text(
                        text = status.first,
                        color = Color.Black,
                        modifier = Modifier
                            .width(300.dp)
                            .height(32.dp)
                            .background(status.second, RoundedCornerShape(50))
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 12.sp
                    )


                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
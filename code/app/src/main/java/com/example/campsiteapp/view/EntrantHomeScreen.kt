package com.example.campsiteapp.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ChatBubbleOutline
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
import com.example.campsiteapp.model.Reservation
import com.example.campsiteapp.ui.theme.CampsiteAppTheme
import java.time.LocalDate

private val pill = RoundedCornerShape(50)

class EntrantHomeScreen : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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
    updateByUserSearch: (String) -> Unit,
    deviceId: String,

    openProfileScreen: () -> Unit,
    openOwnerScreen: () -> Unit,
    openAdminScreen: () -> Unit,
    openEntrantOfferScreen: (lottery: Lottery, hasOffer: Boolean) -> Unit
) {
    var entrantSearch by remember { mutableStateOf("") }
    var menuOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(modifier = modifier.fillMaxSize().padding(top = 5.dp)) {
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

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = "Select Dates",
                    modifier = Modifier
                        .border(1.dp, Color.Black, pill)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "Select Guests",
                    modifier = Modifier
                        .border(1.dp, Color.Black, pill)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(64.dp))

            val reservations = EntrantCampsiteController.getReservationsById(deviceId)
            val lotteries = EntrantCampsiteController.getLotteriesById(deviceId)
            android.util.Log.d("HOME", "deviceId=$deviceId lotteries=${lotteries.size}")


            Text("Your Confirmed Bookings", modifier = Modifier.padding(start = 16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                reservations.forEach { reservation ->
                    displayBookingsAsCards(reservation, modifier)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }


            Spacer(modifier = Modifier.height(32.dp))
            Text("Your Pending Bookings", modifier = Modifier.padding(start = 16.dp))

            displayCampsitesAsCards(
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
                    .clickable {
                        context.startActivity(
                            Intent(
                                context,
                                PastReservationsScreen::class.java
                            )
                        )
                    }
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
fun displayBookingsAsCards(
    reservation: Reservation,
    modifier: Modifier
){
    /*
        the firestore document doesn't keep the serialized object, so
        reservationDB needs to fetch the campsite using the campsiteId as a column
    */
    val campsite: Campsite = reservation.campsite // Reservation needs to fetch the campsite, and then I need to call

    Box(
        modifier = Modifier
            .size(width = 350.dp, height = 170.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(14.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
        ) {
            Text(text = campsite.name, fontSize = 18.sp)
            Text(text = campsite.address, fontSize = 12.sp, color = Color.Gray)
            Text(
                text = "Maximum ${campsite.occupancy} - ${campsite.rating} stars",
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
    }
}
@Composable
fun displayCampsitesAsCards(modifier: Modifier = Modifier,
                            lotteries: List<Lottery>, onClickEvent: (Lottery) -> Unit,
                            getTextAttributes: (Lottery) -> Pair<String, Color>,
){
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        lotteries.forEach { lottery ->
            val campsite: Campsite = lottery.campsite
            val status: Pair<String, Color> = getTextAttributes(lottery)

            Box(modifier = Modifier
                .size(width = 350.dp, height = 170.dp)
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
                    Text(text = "Maximum ${campsite.occupancy} - ${campsite.rating} stars",
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
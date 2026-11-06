package com.example.campsiteapp.view

import android.content.Intent
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import com.example.campsiteapp.model.Campsite
import com.example.campsiteapp.data.DeviceIDs
import com.example.campsiteapp.model.Lottery
import com.example.campsiteapp.ui.theme.CampsiteAppTheme

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
    openAdminScreen: () -> Unit
) {
    var entrantSearch by remember { mutableStateOf("") }
    var menuOpen by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize()
        .padding(top = 5.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
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
                        onClick = { menuOpen = false
                            openAdminScreen()}
                    )
                    DropdownMenuItem(
                        text = { Text("Owner") },
                        onClick = { menuOpen = false }
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

        LazyColumn( modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(EntrantCampsiteController.getReservationsById(deviceId)) { reservation ->
                val campsite: Campsite = reservation.campsite

                Box(modifier = modifier
                    .size(width = 350.dp, height = 140.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(14.dp))
                ){
                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
                    ){
                        Text(text = campsite.name, fontSize = 18.sp)
                        Text(text = campsite.address, fontSize = 12.sp, color = Color.Gray)
                        Text(text = "Maximum ${campsite.occupancy} - ${campsite.rating} stars",
                            fontSize = 12.sp, lineHeight = 10.sp)
                        Text(text = "Available from ${campsite.startDate}", fontSize = 12.sp)

                        val status: Pair<String, Color> = Pair("Booked - Cancel Before ${campsite.startDate}", Color(0xFFB5C7BE))
                        Text(
                            text = status.first,
                            color = Color.Black,
                            modifier = Modifier
                                .width(300.dp)
                                .height(60.dp)
                                .background(status.second, RoundedCornerShape(50))
                                .padding(horizontal = 12.dp, vertical = 4.dp),
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }


        Spacer(modifier = Modifier.height(32.dp))
        Text("      Your Pending Bookings")
        displayCampsitesAsCards(modifier, EntrantCampsiteController.getLotteriesById(deviceId),
            {}, getTextAttributes = { lottery ->
                if (lottery.winner == deviceId) {
                    Pair("Campsite won - respond before ${lottery.bookingEndDate}", Color(0xFFE6DDB0))
                } else {
                    Pair("Entered in lottery", Color(0xFFD9D9D9))
                }
            })

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
                    .clickable { }
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )

            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = "Messages"
                )
            }
        }
    }
}


@Composable
fun displayCampsitesAsCards(modifier: Modifier = Modifier,
                            lotteries: List<Lottery>, onClickEvent: (String) -> Unit,
                            getTextAttributes: (Lottery) -> Pair<String, Color>,
){
    LazyColumn( modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(lotteries) { lottery ->
            val campsite: Campsite = lottery.campsite

            Box(modifier = modifier
                .size(width = 350.dp, height = 140.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(14.dp))
                .clickable { onClickEvent(lottery.id) }
            ){
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
                ){
                    Text(text = campsite.name, fontSize = 18.sp)
                    Text(text = campsite.address, fontSize = 12.sp, color = Color.Gray)
                    Text(text = "Maximum ${campsite.occupancy} - ${campsite.rating} stars",
                        fontSize = 12.sp, lineHeight = 10.sp)
                    Text(text = "Available from ${campsite.startDate}", fontSize = 12.sp)

                    val status = getTextAttributes(lottery)

                    Text(
                        text = status.first,
                        color = Color.Black,
                        modifier = Modifier
                            .width(300.dp)
                            .height(60.dp)
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

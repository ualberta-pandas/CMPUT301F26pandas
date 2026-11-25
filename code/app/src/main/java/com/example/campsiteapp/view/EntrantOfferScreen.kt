package com.example.campsiteapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.IntentCompat
import com.example.campsiteapp.controller.EntrantCampsiteController
import com.example.campsiteapp.controller.EntrantOfferController
import com.example.campsiteapp.data.DeviceIDs
import com.example.campsiteapp.data.Reservation
import com.example.campsiteapp.model.Campsite
import com.example.campsiteapp.model.Lottery
import com.example.campsiteapp.ui.theme.CampsiteAppTheme
private val pill = RoundedCornerShape(50)

class EntrantOfferScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // TODO: get these from your controllers / intent
        val lottery = IntentCompat.getSerializableExtra(intent, "lottery", Lottery::class.java)
            ?: error("EntrantOfferScreen needs a lottery extra")
        val campsite = lottery.campsite
        val hasOffer = intent.getBooleanExtra("hasOffer", false)

        setContent {
            val context = LocalContext.current
            val deviceId by produceState(initialValue = "") {
                value = DeviceIDs(context).fetchDeviceId() ?: ""
            }

            CampsiteAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (deviceId.isNotEmpty()) EntrantOfferView(
                        campsiteSelection = campsite,
                        lottery = lottery,
                        hasOffer = hasOffer,
                        deviceId = deviceId,

                        openProfileScreen = {
                            EntrantCampsiteController.toggleProfileScreen(this@EntrantOfferScreen)
                        },
                        openAdminScreen = {
                            EntrantCampsiteController.toggleAdminScreen(this@EntrantOfferScreen)
                        },
                        openOwnerScreen = {
                            EntrantCampsiteController.toggleOwnerScreen(this@EntrantOfferScreen)
                        },
                        openMenuScreen = {
                            EntrantCampsiteController.toggleMenuScreen(this@EntrantOfferScreen)
                        },

                        acceptUserOffer = {deviceId, lottery ->
                            EntrantOfferController.entrantAcceptOfferHandler(deviceId, lottery)

                            // TODO: Controller here to accept the user offer
                        },

                        rejectUserOffer = {deviceId, lottery ->
                            EntrantOfferController.entrantRejectOfferHandler(deviceId, lottery)
                            // TODO: Controller here to reject the user offer
                        },

                        downloadUserOffer = { deviceId, reservation ->
                            EntrantOfferController.downloadUserOffer(this@EntrantOfferScreen, deviceId, reservation)
                        },

                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun EntrantOfferView(
    campsiteSelection: Campsite,
    lottery: Lottery,
    hasOffer: Boolean,
    deviceId: String,

    openProfileScreen: () -> Unit,
    openOwnerScreen: () -> Unit,
    openAdminScreen: () -> Unit,
    openMenuScreen: () -> Unit,

    acceptUserOffer: (deviceId: String, lottery: Lottery) -> Reservation,
    rejectUserOffer: (deviceId: String, lottery: Lottery) -> Unit,
    downloadUserOffer: (deviceId: String, reservation: Reservation) -> Unit,

    modifier: Modifier = Modifier
) {

    var entrantSearch by remember { mutableStateOf("") }
    var menuOpen by remember { mutableStateOf(false) }
    val offerAccepted = remember { mutableStateOf(false) }
    val offerRejected = remember { mutableStateOf(false) }

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
                            openAdminScreen()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Owner") },
                        onClick = { menuOpen = false
                            openOwnerScreen()
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Main Menu") },
                        onClick = { menuOpen = false
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
        }

        BasicTextField(
            value = entrantSearch,
            onValueChange = { entrantSearch = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),

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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f)
                .padding(horizontal = 16.dp, vertical = 0.dp)
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
        ){
            Text(
                text = "Select Dates",
                modifier = Modifier
                    .border(1.dp, Color.Black, pill)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                fontSize = 13.sp
            )

            Text(
                text = "Select Guest Count",
                modifier = Modifier
                    .border(1.dp, Color.Black, pill)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                fontSize = 13.sp
            )
        }

        val reservation = remember { mutableStateOf<Reservation?>(null) }

        if (hasOffer && !offerAccepted.value){
            AcceptRejectOffer(lottery, acceptUserOffer, rejectUserOffer,
                offerAccepted = offerAccepted,
                offerRejected = offerRejected,
                deviceId = deviceId,
                reservation = reservation
                )
        }

        if (offerAccepted.value || offerRejected.value){
            DownloadOffer(
                downloadUserOffer,
                offerRejected,
                deviceId = deviceId,
                reservation = reservation
                )
        }
    }
}

@Composable
fun AcceptRejectOffer(
    lottery: Lottery,
    acceptUserOffer: (String, Lottery) -> Reservation,
    rejectUserOffer: (String, Lottery) -> Unit,

    offerAccepted: MutableState<Boolean>,
    offerRejected: MutableState<Boolean>,
    reservation: MutableState<Reservation?>,

    deviceId: String
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Offer Deadline ${lottery.bookingEndDate}",
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
    // Color(0xFFC0392B)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal= 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {
        Button(
            onClick = {
                offerAccepted.value = true
                offerRejected.value = false
                reservation.value = acceptUserOffer(deviceId, lottery)
            },
            shape = pill,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB5C7BE)),
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .height(40.dp)
        ) {
            Text(
                text = "Accept Offer",
                fontSize = 16.sp
            )
        }

        Button(
            onClick = {
                offerRejected.value = true
                offerAccepted.value = false
                rejectUserOffer(deviceId, lottery)
            },
            shape = pill,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0392B)),
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .height(40.dp)
        ) {
            Text(
                text = "Reject Offer",
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun DownloadOffer(
    downloadUserOffer: (deviceId: String, reservation: Reservation) -> Unit,
    offerRejected: MutableState<Boolean>,

    deviceId: String,
    reservation: MutableState<Reservation?>
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ){
        Text(
            text = if (offerRejected.value) "Offer Accepted" else "Offer Rejected",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .background(Color(0xFFB5C7BE), pill)
                .height(40.dp)
                .wrapContentHeight(Alignment.CenterVertically)
        )
    }

    if (!offerRejected.value) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
        ) {
            OutlinedButton(
                onClick = {
                    reservation.value?.let { downloadUserOffer(deviceId, it) } // to handle the NULL value
                },
                shape = pill,
                modifier = Modifier
                    .width(64.dp)
                    .padding(8.dp)
                    .height(40.dp)
            ) {
                Icon(Icons.Outlined.Download, contentDescription = "download confirmation CSV")
            }
        }
    }
}
package com.example.campsiteapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Download

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.IntentCompat
import com.example.campsiteapp.controller.EntrantCampsiteController
import com.example.campsiteapp.model.Campsite
import com.example.campsiteapp.model.Lottery
import com.example.campsiteapp.ui.theme.CampsiteAppTheme

import com.example.campsiteapp.data.DeviceIDs
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

                        openProfileScreen = { EntrantCampsiteController.toggleProfileScreen(this@EntrantOfferScreen) },
                        openAdminScreen = { EntrantCampsiteController.toggleAdminScreen(this@EntrantOfferScreen) },
                        openOwnerScreen = { EntrantCampsiteController.toggleOwnerScreen(this@EntrantOfferScreen) },
                        openMenuScreen = {EntrantCampsiteController.toggleMenuScreen(this@EntrantOfferScreen)},
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

    openProfileScreen: () -> Unit,
    openOwnerScreen: () -> Unit,
    openAdminScreen: () -> Unit,
    openMenuScreen: () -> Unit,


    modifier: Modifier = Modifier
) {
    var entrantSearch by remember { mutableStateOf("") }
    var menuOpen by remember { mutableStateOf(false) }
    var offerAccepted by remember {mutableStateOf(false)}

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

        if (hasOffer && !offerAccepted) {
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
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ) {
                Button(
                    onClick = {
                        offerAccepted = true
                    },
                    shape = pill,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB5C7BE)),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .height(40.dp)
                ) {
                    Text("Accept Offer", fontSize = 16.sp)
                }

                Button(
                    onClick = {},
                    shape = pill,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0392B)),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .height(40.dp)
                ) {
                    Text("Reject Offer", fontSize = 16.sp)
                }
            }
        }

        if (offerAccepted){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ){
                Text(
                    text = "Offer Accepted",
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ){
                OutlinedButton(
                    onClick = {},
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
}


@Preview(showBackground = true)
@Composable
fun CampsiteViewPreview() {
    val campsite = Campsite(
        id = "c1",
        name = "ABC Park",
        location = "123 Jasper, Edmonton, AB",
        occupancy = 8.0,
        rating = 4.4,
        startDate = "2026-09-22",
        description = "Campsite description open - lorem ipsum dolor sit amet.\n\nAmenities\n• Running water on site\n• Grill on site"
    )
    val lottery = Lottery(
        id = "l1",
        campsite = campsite,
        lotteryStartDate = "2026-09-23",
        lotteryEndDate = "2026-10-30",
        bookingEndDate = "2027-03-03",
        waitlistMax = 50,
        winners = arrayListOf("abc123")
    )
    CampsiteAppTheme {
        EntrantOfferView(
            campsiteSelection = campsite,
            lottery = lottery,
            hasOffer = true,
            openProfileScreen = {},
            openOwnerScreen = {},
            openAdminScreen = {},
            openMenuScreen = {}
        )
    }
}
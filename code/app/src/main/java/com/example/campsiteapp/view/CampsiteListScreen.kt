package com.example.campsiteapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campsiteapp.model.Campsite
import com.example.campsiteapp.ui.theme.CampsiteAppTheme

import com.example.campsiteapp.model.Lottery

import com.example.campsiteapp.controller.EntrantCampsiteController

import com.example.campsiteapp.controller.EntrantCampsiteController.toggleSearchResultsScreen
import com.example.campsiteapp.controller.EntrantCampsiteController.toggleCampsiteDetailsScreen

private val pill = RoundedCornerShape(50)

class CampsiteListScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // TODO: get these from your controllers /

        val searchResult = intent.getStringExtra("search") ?: ""

        // declare intent here

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
                        searchResult
                    )
                }
            }
        }
    }
}




@Composable
fun CampsiteListView(
    modifier: Modifier = Modifier,
    updateByUserSearch: (search: String) -> Unit,
    onLotteryClick: (String) -> Unit,

    lotteries: List<Lottery>,
    searchResult: String
){
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

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = "No Dates Filter",
                modifier = Modifier
                    .border(1.dp, Color.Black, pill)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = "No Guest Filter",
                modifier = Modifier
                    .border(1.dp, Color.Black, pill)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )


        }

        Spacer(modifier = Modifier.height(15.dp))

        displayCampsitesAsCards(Modifier, lotteries = lotteries, onLotteryClick,
            getTextAttributes = {lottery ->
                val currSize = lottery.waitlistMax - lottery.waitlist.size
                if (currSize == 0) {
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

/*
// Just generated random values using Claude for the preview as I'm developing
@Preview(showBackground = true)
@Composable
fun CampsiteListViewPreview() {
    val campsiteA = Campsite(
        id = "1",
        name = "Campsite Location A",
        owner = "dana_device_id",
        location = "123 Jasper, Edmonton, AB",
        startDate = "2026-09-22",
        endDate = "2026-10-30",
        rating = 4.4,
        occupancy = 8.0,
        description = "Open campsite near the lake.",
        latitude = "53.54",
        longitude = "-113.49",
        price = "45.0",
        tags = "",
        rules = "Quiet hours after 10pm",
        image = "",
        status = "open",
        address = "6702 280 Bays Drive"
    )

    val campsiteB = Campsite(
        id = "2",
        name = "Campsite Location B",
        owner = "dana_device_id",
        location = "45 Banff Ave, Banff, AB",
        startDate = "2026-09-25",
        endDate = "2026-11-01",
        rating = 3.9,
        occupancy = 4.0,
        description = "Quiet site in the trees.",
        latitude = "51.18",
        longitude = "-115.57",
        price = "30.0",
        tags = "",
        rules = "No fires",
        image = "",
        status = "open",
        address = "5678 Southbay West"
    )

    val lotteries = arrayListOf(
        // waitlistMax - waitlist.size = 0  -> "Closed - No Space Left"
        Lottery(
            campsiteId = 1,
            numWinners = 5,
            lotteryStartDate = "2026-09-16",
            lotteryEndDate = "2026-09-23",
            waitlistMax = 2,
            waitlist = arrayListOf("abc123", "xyz789"),
            campsite = campsiteA
        ),
        // 1 left -> "Open - 2 Spaces remaining"
        Lottery(
            campsiteId = 2,
            numWinners = 3,
            lotteryStartDate = "2026-09-18",
            lotteryEndDate = "2026-09-25",
            waitlistMax = 3,
            waitlist = arrayListOf("abc123", "xyz789"),
            campsite = campsiteB
        ),
        // many left -> "Open - Many Spots"
        Lottery(
            campsiteId = 3,
            numWinners = 5,
            lotteryStartDate = "2026-09-20",
            lotteryEndDate = "2026-09-27",
            waitlistMax = 50,
            waitlist = arrayListOf("abc123", "xyz789"),
            campsite = campsiteA
        )
    )

    CampsiteAppTheme {
        CampsiteListView(
            updateByUserSearch = {},
            onLotteryClick = {},
            lotteries = lotteries,
            searchResult = "e"
        )
    }
}
*/
/*
Prompts to
okay, I know that if you change something in a list, when there's a lazy_column that's relying on it, its going to recompose because of
the update to the list. give me some sample code and things to get started

generate me some campsite test-cases


working with boxes (for visual effects)
*/
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
                        searchResult = searchResult,
                        openProfileScreen = { EntrantCampsiteController.toggleProfileScreen(this@CampsiteListScreen) },
                        openAdminScreen = { EntrantCampsiteController.toggleAdminScreen(this@CampsiteListScreen) },
                        openOwnerScreen = { EntrantCampsiteController.toggleOwnerScreen(this@CampsiteListScreen) }

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

    openProfileScreen: () -> Unit,
    openOwnerScreen: () -> Unit,
    openAdminScreen: () -> Unit,

    lotteries: List<Lottery>,
    searchResult: String
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
                        onClick = { menuOpen = false }
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

        displayCampsitesAsCards(
            Modifier, lotteries = lotteries, onLotteryClick,
            getTextAttributes = { lottery ->
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
Prompts to
okay, I know that if you change something in a list, when there's a lazy_column that's relying on it, its going to recompose because of
the update to the list. give me some sample code and things to get started

generate me some campsite test-cases


working with boxes (for visual effects)
*/
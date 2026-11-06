package com.example.campsiteapp.view

import android.content.Intent
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campsiteapp.controller.EntrantCampsiteController
import com.example.campsiteapp.ui.theme.CampsiteAppTheme

private val pill = RoundedCornerShape(50)

class EntrantHomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // TODO: get these from your controllers / intent
        setContent {
            CampsiteAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    EntrantHomeScreenView(
                        modifier = Modifier.padding(innerPadding),
                        updateByUserSearch = { search ->
                            EntrantCampsiteController.toggleSearchResultsScreen(this@EntrantHomeScreen, search)
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
    updateByUserSearch: (search: String) -> Unit
) {
    var entrantSearch by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize()
        .padding(top = 5.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(5.dp))

        IconButton(
            modifier = Modifier,
            onClick = {}) {
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
        Text("      Your Confirmed Bookings")
        LazyColumn(modifier = Modifier.weight(1f)) {

        }

        Spacer(modifier = Modifier.height(32.dp))
        Text("      Your Pending Bookings")
        LazyColumn(modifier = Modifier.weight(1f)) {

        }

        val context = LocalContext.current
        OutlinedButton(
            onClick = { context.startActivity(Intent(context, PastReservationsScreen::class.java)) },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .testTag("pastReservationsButton")
        ) {
            Text("See past reservations")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EntrantHomeScreenViewPreview() {
    EntrantHomeScreenView(updateByUserSearch = {})
}
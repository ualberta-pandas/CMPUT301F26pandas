package com.example.campsiteapp.view.components

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


import androidx.compose.ui.platform.LocalContext
import com.example.campsiteapp.controller.EntrantCampsiteController
import com.example.campsiteapp.controller.EntrantCampsiteController.toggleSearchResultsScreen

private val pill = RoundedCornerShape(50)

@Composable
fun EntrantScreenHeader(
    context: Activity,
    modifier: Modifier,
    updateByUserSearch: (search: Triple<String, String, String>) -> Unit,
    availableDates: String = "Select Dates",
    guestCount: String = "0",
){
    var entrantSearch by remember { mutableStateOf("") }
    var menuOpen by remember { mutableStateOf(false) }
    var availableDates by remember { mutableStateOf("Select Dates") }
    var guestCount by remember { mutableStateOf("Guest Count") }
    val filters = Triple(entrantSearch,availableDates,guestCount)

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
                        EntrantCampsiteController.toggleAdminScreen(context)
                    }
                )
                DropdownMenuItem(
                    text = { Text("Owner") },
                    onClick = { menuOpen = false
                        EntrantCampsiteController.toggleOwnerScreen(context)
                    }
                )

                DropdownMenuItem(
                    text = { Text("Main Menu") },
                    onClick = { menuOpen = false
                        EntrantCampsiteController.toggleMenuScreen(context)
                    }
                )
            }
        }


        IconButton(
            modifier = Modifier.background(Color(0xFFE8DEF8), CircleShape),
            onClick = {
                EntrantCampsiteController.toggleProfileScreen(context)
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
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    )
}
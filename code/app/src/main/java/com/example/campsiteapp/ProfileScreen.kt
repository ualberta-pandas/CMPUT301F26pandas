package com.example.campsiteapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController

@Composable
fun ProfileScreen(navController: NavController, user: User, profile: User){
    //display profile details


    if (user==profile){
        //if user is the same as the profile, is in "My Profile" mode
        var profileScreenMode by remember {
            mutableStateOf("My Profile")
        }

    } else {
        //else, is in "View Profile" mode
        var profileScreenMode by remember {
            mutableStateOf("View Profile")
        }
    }

    //profile picture
    //profile name
    //profile email
    //profile phone number
    //profile info
}
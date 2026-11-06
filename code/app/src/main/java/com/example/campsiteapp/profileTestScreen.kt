package com.example.campsiteapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.campsiteapp.controller.ProfileDatabase
import com.example.campsiteapp.model.User
import com.example.campsiteapp.view.ProfileScreen

@Composable
fun ProfileTestScreen(){
    val navController = rememberNavController()
    val profileDatabase = ProfileDatabase()
    val testUser = User()
    testUser.deviceId="Test"
    testUser.profileImage="https://res.cloudinary.com/lgwzmfuq/image/upload/v1793356570/rnspqpddbpolylgh3qrh.jpg"
    testUser.phone="Phone number"
    testUser.email="Email"
    testUser.name="Name"
    testUser.deviceId="deviceId"
    testUser.roles=mutableListOf("Entrant")
    profileDatabase.addProfile(testUser)
    val profileScreen= ProfileScreen()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { ProfileHomeScreen(navController) }
        composable("has profile") { profileScreen.DisplayProfileScreen(navController, "deviceId", testUser.roles,null,
            onAddUser = { profileDatabase.addProfile(it)},
            onDeleteUser = { profileDatabase.deleteProfile(it)},
            onUpdateUser = {oldProfile,newProfile -> profileDatabase.updateProfile(oldProfile,newProfile) }
        ) }
        composable("view profile") { profileScreen.DisplayProfileScreen(navController, "otherDeviceId", testUser.roles,testUser,
            onAddUser = { profileDatabase.addProfile(it)},
            onDeleteUser = { profileDatabase.deleteProfile(it)},
            onUpdateUser = {oldProfile,newProfile -> profileDatabase.updateProfile(oldProfile,newProfile) }
        ) }
    }
}
@Composable
fun ProfileHomeScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Test Home",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("has profile")
            },
            modifier = Modifier.testTag("has profile button") // for testing
        ) {
            Text("own profile")
        }

        Button(
            onClick = {
                navController.navigate("view profile")
            },
            modifier = Modifier.testTag("view profile button") // for testing
        ) {
            Text("has profile")
        }
    }
}
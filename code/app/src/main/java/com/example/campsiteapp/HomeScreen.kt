package com.example.campsiteapp


import android.R
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.campsiteapp.data.CampsitesDatabase
import com.example.campsiteapp.model.User
import com.example.campsiteapp.ui.theme.CampsiteAppTheme
import com.example.campsiteapp.view.AdminApp
import com.example.campsiteapp.view.OwnerCampsiteListScreen
import com.example.campsiteapp.view.ProfileScreen
import com.example.campsiteapp.view.components.ScreenScaffold
import kotlinx.coroutines.launch
import kotlin.String

@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun ScreenNavigation(modifier: Modifier) {
    val navController = rememberNavController()

}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    onProfileClick: () -> Unit = {},
    onModesClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Helper: close the drawer, then run the click action
    fun closeAnd(action: () -> Unit) {
        scope.launch { drawerState.close() }
        action()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Menu",
                    modifier = Modifier.padding(16.dp),
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge
                )
                NavigationDrawerItem(
                    label = { Text("Profile") },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    selected = false,
                    onClick = { closeAnd(onProfileClick) },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Modes") },
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    selected = false,
                    onClick = { closeAnd(onModesClick) },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Notifications") },
                    icon = { Icon(Icons.Default.Notifications, contentDescription = null) },
                    selected = false,
                    onClick = { closeAnd(onNotificationsClick) },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        // Tap this to open the sidebar
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Open menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                NavHost(navController = navController, startDestination = "Modes") {

                    composable("Modes") {
                            Column(
                                Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button({
                                    navController.navigate("admin")
                                }
                                ) {
                                    Text("Admin Mode")
                                }
                                Button({
                                    navController.navigate("entrant")
                                }
                                ) {
                                    Text("Entrant Mode")
                                }
                                Button({
                                    navController.navigate("owner")
                                }
                                ) {
                                    Text("Owner Mode")
                                }
                            }

                    }
                    composable ("profile"){
                        val testDeviceId = "testDeviceId"
                        val testUser = null
                        //User()
                        //testUser.deviceId="Test"
                            ProfileScreen(
                                navController,
                                testDeviceId,
                                mutableListOf<String>("entrant"),
                                testUser
                            )

                    }
                    composable ("notifications"){
                            //not implemented(?)

                    }
                    composable ("admin"){
                            AdminApp(CampsitesDatabase())

                    }
                    composable ("entrant"){


                            //not implemented(?)


                    }
                    composable ("owner"){
                        val campsitesDatabase = CampsitesDatabase()

                            OwnerCampsiteListScreen().CampsitesList(
                                campsites = campsitesDatabase.campsites,
                                onAddCampsite = { campsite -> campsitesDatabase.addCampsite(campsite) },
                                onEditCampsite = { oldCampsiteDetails, newCampsiteDetails ->
                                    campsitesDatabase.updateCampsite(
                                        oldCampsiteDetails,
                                        newCampsiteDetails = newCampsiteDetails
                                    )
                                }
                            )


                    }
                }


            }
        }
    }
}
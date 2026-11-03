package com.example.campsiteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.campsiteapp.ui.theme.CampsiteAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val campsitesDatabase = CampsitesDatabase()

        setContent {
            CampsiteAppTheme {
                Campsite().CampsitesList(
                    campsites = campsitesDatabase.campsites,
                    onAddCampsite = {campsitesDatabase.addCampsite(it)}
                )
            }
        }

        // Uncomment to show the admin screens; this replaces the content set above.
        // setContent {
        //     CampsiteAppTheme {
        //         com.example.campsiteapp.view.AdminApp(campsitesDatabase)
        //     }
        // }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CampsiteAppTheme {
        Greeting("Android")
    }
}
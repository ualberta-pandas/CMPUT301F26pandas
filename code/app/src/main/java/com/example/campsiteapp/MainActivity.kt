/*
 * MainActivity.kt
 *
 * Entry point of the app. Sets up the theme and shows the admin screens.
 *
 * Outstanding issues:
 * - Always opens the admin screens; should route by the current user's role
 *   once entrant and owner screens exist.
 */
package com.example.campsiteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.campsiteapp.ui.theme.CampsiteAppTheme
import com.example.campsiteapp.view.AdminApp

class MainActivity : ComponentActivity() {
    private val adminController by lazy { AppContainer.adminController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampsiteAppTheme {
                AdminApp(controller = adminController)
            }
        }
    }
}

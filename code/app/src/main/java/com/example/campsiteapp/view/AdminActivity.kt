package com.example.campsiteapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.campsiteapp.data.CampsitesDatabase
import com.example.campsiteapp.ui.theme.CampsiteAppTheme

/**
 * Activity that shows [AdminApp].
 */
class AdminActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val campsitesDatabase = CampsitesDatabase()
        setContent {
            CampsiteAppTheme {
                AdminApp(campsitesDatabase)
            }
        }
    }
}

package com.example.campsiteapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.campsiteapp.data.NotificationDatabase

class NotificationsActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val notifications = NotificationDatabase().notifications

            NotificationsList(
                notifications = notifications,
                onBack = {finish()}

            )
        }
    }
}
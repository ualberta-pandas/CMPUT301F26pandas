package com.example.campsiteapp.view


import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campsiteapp.model.Notification

@Composable
fun NotificationsList(
    notifications: List<Notification>,
    onBack: () -> Unit
) {

    Button(
        onClick = {onBack()}
    ) { Text("<-")}
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        items(notifications) { notification ->
            NotificationCard(
                notification = notification
            )
        }
    }
}

@Composable
fun NotificationCard(
    notification: Notification
) {
    Box(
        modifier = Modifier
            .width(350.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))

    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(text = notification.description, fontSize = 18.sp)
            Text(text = notification.receiver, fontSize = 12.sp, color = Color.Gray)
            Text(text = notification.dateSent, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

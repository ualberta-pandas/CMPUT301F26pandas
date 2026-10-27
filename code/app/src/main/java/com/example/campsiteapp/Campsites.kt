package com.example.campsiteapp

import android.app.Dialog
import android.media.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.content.MediaType.Companion.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.window.Dialog


class Campsites {

@Composable
fun CampsitesList(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit

) {


    var showRegistration by remember { mutableStateOf(false) }
    var campName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .border(
                    width = 2.dp,
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Black
                )
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = "Campsite Name",
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Location"
                )

                Text(
                    text = "# People",
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Status",
                    modifier = Modifier.background(Color.Green)
                        .border(
                            width =1.dp,
                            color = Color.Green,
                            shape = RoundedCornerShape(size = 20.dp)
                        )
                )
            }

            Spacer(modifier = Modifier.width(80.dp))

            Column() {
                Image(
                    painter = painterResource(
                        id = R.drawable.ic_launcher_background
                    ),
                    contentDescription = "A cat",
                    modifier = Modifier.size(120.dp)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ) {
            FloatingActionButton(
                onClick = {
                    showRegistration = true
                }
            ) { Text(text = "+", fontSize = 20.sp)}
        }

        if (showRegistration){
            Dialog(
              onDismissRequest = { onDismissRequest()}
            ){
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Add Campsite"
                        )

                        TextButton(
                            onClick = { onDismissRequest() },
                            modifier = Modifier.border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(50)
                            )
                        ) { Text("x") }
                    }


                    TextField(
                        value = campName,
                        onValueChange = {campName = it},
                        label = {
                            Text("Campsite Name")
                                },
                        modifier = Modifier.testTag("campTextField")
                    )

                }
            }
                }
        }

    }
}

@Composable
fun Lottery(){

}

@Composable
fun Reservations(){

}

}
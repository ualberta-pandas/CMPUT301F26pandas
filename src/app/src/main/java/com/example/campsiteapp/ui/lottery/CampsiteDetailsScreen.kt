package com.example.campsiteapp.ui.lottery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.campsiteapp.ui.theme.CampsiteAppTheme

import com.example.campsiteapp.data.Campsite
import com.example.campsiteapp.data.Lottery
import java.time.format.DateTimeFormatter

class CampsiteDetailsScreen:  ComponentActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampsiteAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CampsiteView(
                        modifier = Modifier.padding(innerPadding)
                    )

                }
            }
        }
    }
}

@Composable
fun CampsiteView(modifier: Modifier = Modifier,
                 campsiteSelection: Campsite
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        // hello, Name

        // Search bar here

        //date and count
        Row(
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.Start)
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ){
            Text(
                text = """"
                    ${campsiteSelection.lottery?.lotteryStartDate?.format(DateTimeFormatter.ofPattern("MMM d"))} 
                    - ${campsiteSelection.lottery?.lotteryEndDate?.format(DateTimeFormatter.ofPattern("MMM d"))}""".trimIndent(),
                modifier = Modifier
                    .border(1.dp, Color.Black, RoundedCornerShape(50))
            )

            Text(
                text = "< back to results",
                modifier = Modifier
                    .border(1.dp, Color.Black, RoundedCornerShape(50))
            )

            Text(
                text = """
                    Campsite Location A
                    ${campsiteSelection.location}
                    ${campsiteSelection.lottery?.waitlistMax} people maximum occupancy
                    ${campsiteSelection.rating}/5 
                     """, // Not sure what the A means here, and need to add star sign here
                modifier = Modifier
                    .border(1.dp, Color.Black, RoundedCornerShape(50))
            )
        }

        Row(
            modifier = Modifier.fillMaxSize()
                .weight(0.5f),
        ){
            Column(
                modifier = Modifier.fillMaxSize()
            ){
                Text(
                    text = "Campsite Location A"
                )

                Text(
                    text = "Image goes here"
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = campsiteSelection.description
                )
            }
        }
    }

    Text(
        text = "Available starting ${campsiteSelection.startDate.format(DateTimeFormatter.ofPattern("MMM d"))}"
    )

    Button(
        onClick = {},
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors( // going to be some if statement here saying if the user's device_id is already under lottery
            containerColor = Color(0xFFB5C7BE),
            contentColor = Color(0xFF2F5240)
        )
    ) {
        Text("Join Waitlist", fontSize = 20.sp)
    }
}




@Preview(showBackground = true)
@Composable
fun EntrantLotteryScreenPreview() {
    CampsiteAppTheme {
        CampsiteView()
    }
}
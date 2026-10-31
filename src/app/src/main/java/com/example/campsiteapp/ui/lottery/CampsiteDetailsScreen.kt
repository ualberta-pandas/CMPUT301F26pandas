package com.example.campsiteapp.ui.lottery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.example.campsiteapp.data.Campsite
import com.example.campsiteapp.data.Lottery
import com.example.campsiteapp.ui.theme.CampsiteAppTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val dateFormat = DateTimeFormatter.ofPattern("MMM d")
private val pill = RoundedCornerShape(50)

class CampsiteDetailsScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // TODO: get these from your controllers / intent
        val campsite = intent.getSerializableExtra("campsite") as Campsite
        val lottery = intent.getSerializableExtra("lottery") as Lottery?
        val isJoined = intent.getBooleanExtra("isJoined", false)

        setContent {
            CampsiteAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CampsiteView(
                        campsiteSelection = campsite,
                        lottery = lottery,
                        isJoined = isJoined,
                        onJoinClick = {
                            /*
                            if (isJoined) waitlistController.leave(campsite, lottery!!)
                            else waitlistController.join(campsite, lottery!!)

                             */
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun CampsiteView(
    campsiteSelection: Campsite,
    lottery: Lottery?,
    isJoined: Boolean,
    onJoinClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // TODO: "Hello, Name" and search bar

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            lottery?.let { // if the lottery is not nan then show this
                Text(
                    text = "${it.lotteryStartDate.format(dateFormat)} - ${it.lotteryEndDate.format(dateFormat)}",
                    modifier = Modifier
                        .border(1.dp, Color.Black, pill)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            lottery?.let{
                Text(
                    text = "Guest Count: ${campsiteSelection.maxOccupancy}",
                    modifier = Modifier
                        .border(1.dp, Color.Black, pill)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()

        ){
            Button(
                onClick = onJoinClick,
                shape = pill,
                border = BorderStroke(1.dp, Color.Black),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Black)
            ) {
                Text("< back to results")
            }
        }



        Text(
            text = """
                ${campsiteSelection.name}
                ${campsiteSelection.location}
                ${campsiteSelection.maxOccupancy} people maximum occupancy
                ${campsiteSelection.rating}/5 ★
            """.trimIndent(),
            fontSize = 12.sp,

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .background(Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = campsiteSelection.name, fontSize = 22.sp)

            Text(text = """
                
                
                IMAGE GOES HERE 
                
                
    
            """.trimIndent()) // TODO: promo image

            Spacer(Modifier.height(10.dp))

            Text(text = campsiteSelection.description)
        }

        Text(
            text = "Available starting ${campsiteSelection.startDate.format(dateFormat)}",
            modifier = Modifier
                .border(1.dp, Color.Black, pill)
                .padding(horizontal = 12.dp, vertical = 4.dp)
        )

        if (lottery != null) {
            Button(
                onClick = onJoinClick,
                enabled = isJoined || !lottery.isFull(),
                shape = pill,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB5C7BE),
                    contentColor = Color(0xFF2F5240)
                ),
                modifier = Modifier.padding(16.dp)
            ) {
                Text(if (isJoined) "Leave Waitlist" else "Join Waitlist", fontSize = 20.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CampsiteViewPreview() {
    val campsite = Campsite(
        name = "Campsite Location A",
        owner = "dana_device_id",
        location = "123 Jasper, Edmonton, AB",
        startDate = LocalDate.of(2026, 9, 22),
        endDate = LocalDate.of(2026, 10, 30),
        rating = 4.4f,
        maxOccupancy = 8,
        description = "Open campsite near the lake.\n\nAmenities\n• Running water on site\n• Grill on site\n• Great views"
    )

    val lottery = Lottery(
        campsiteId = 1,
        numWinners = 5,
        lotteryStartDate = LocalDate.of(2026, 9, 16),
        lotteryEndDate = LocalDate.of(2026, 9, 23),
        waitlistCount = 12,
        waitlistMax = 50,
        waitlist = arrayListOf("abc123", "xyz789"),
        campsite = campsite
    )

    CampsiteAppTheme {
        CampsiteView(
            campsiteSelection = campsite,
            lottery = lottery,
            isJoined = false,
            onJoinClick = {}
        )
    }
}
package com.example.campsiteapp.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.campsiteapp.model.Lottery


private val pill = RoundedCornerShape(50)

@Composable
fun CampsiteDetailsCard(
    modifier: Modifier,
    lottery: Lottery,
    ExitHandler: () -> Unit
){
    val campsite = lottery.campsite
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            lottery.let { // if the lottery is not nan then show this
                Text(
                    text = "${lottery.lotteryStartDate} - ${lottery.lotteryEndDate}",
                    modifier = Modifier
                        .border(1.dp, Color.Black, pill)
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            lottery.let {
                Text(
                    text = "Guest Count: ${campsite.occupancy.toInt()}",
                    modifier = Modifier
                        .border(1.dp, Color.Black, pill)
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    fontSize = 12.sp
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Text(
                "< back to results",
                fontSize = 10.sp,
                modifier = Modifier
                    .clickable { ExitHandler() }
                    .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
                    .size(width = 100.dp, height = 30.dp)
            )
        }

        Text(
            text = """
                    ${campsite.name}
                    ${campsite.location}
                    ${campsite.occupancy} people maximum occupancy
                    ${campsite.rating}/5 ★
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
            Text(text = campsite.name, fontSize = 22.sp)

            AsyncImage(
                model = campsite.image,
                contentDescription = "Campsite image",
                modifier = Modifier
                    .testTag("campsiteImage")
                    .width(200.dp)
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(10.dp))

            Text(text = campsite.description)
        }
    }
}

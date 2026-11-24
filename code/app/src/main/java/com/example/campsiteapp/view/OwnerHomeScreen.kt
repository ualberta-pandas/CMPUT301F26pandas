package com.example.campsiteapp.view


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.campsiteapp.controller.LotteryController
import com.example.campsiteapp.data.CampsitesDatabase
import com.example.campsiteapp.data.LotteryDatabase
import com.example.campsiteapp.model.Campsite
import com.example.campsiteapp.view.OwnerCampsiteListScreen
import com.example.campsiteapp.view.LotteryOwnerScreen
import com.example.campsiteapp.model.Lottery


@RequiresApi(Build.VERSION_CODES.O)
@Composable

fun OwnerHomeScreen(
    campsitesDatabase: CampsitesDatabase,
    lotteryDatabase: LotteryDatabase,
    onBack: () -> Unit
) {
    val tabs = listOf("Manage", "Lottery", "Reservations")
    val lotteryController = remember { LotteryController(lotteryDatabase) }
    val lotteryScreen = remember { LotteryOwnerScreen() }
    var selectedIndex by remember { mutableIntStateOf(0) }


    Column(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {

                }
            ) { Text("<-") }
            Text(text = "My Campsites")
        }

        SecondaryTabRow(
            selectedTabIndex = selectedIndex,
            containerColor = Color.Transparent,
            divider = {},
            indicator = {
                Box(
                    Modifier
                        .tabIndicatorOffset(selectedIndex)
                        .padding(horizontal = 12.dp)
                        .height(3.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFF25402F))
                )
            }
        ) {
            tabs.forEachIndexed { index, label ->
                Tab(
                    selected = index == selectedIndex,
                    onClick = { selectedIndex = index },
                    text = { Text(label, fontWeight = FontWeight.SemiBold) },
                    selectedContentColor = MaterialTheme.colorScheme.onSurface,
                    unselectedContentColor =
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        when (selectedIndex) {
            0 -> OwnerCampsiteListScreen().CampsitesList(
                campsites = campsitesDatabase.campsites,
                onAddCampsite = { campsitesDatabase.addCampsite(it) },
                onEditCampsite = { old, new ->

                    campsitesDatabase.updateCampsite(old, new)

                })
            1 -> LotteryOwnerScreen().LotteryList(
                lotteries = lotteryDatabase.lotteries,
                campsites = campsitesDatabase.campsites,
                onCreateLottery = lotteryController::createLottery,
                onUpdateLottery = lotteryController::updateLottery
            )

            // 2 -> ReservationsContent()
        }
    }
}







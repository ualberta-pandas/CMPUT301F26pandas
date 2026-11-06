import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campsiteapp.data.Campsite
import com.example.campsiteapp.view.OwnerCampsiteListScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OwnerHomeScreen(
    campsites: List<Campsite>,
    onAddCampsite: (Campsite) -> Unit,
    onEditCampsite: (Campsite, Campsite) -> Unit
) {
    val tabs = listOf("Manage", "Lottery", "Reservations")
    var selectedIndex by remember { mutableStateOf(0) }

    Column {

        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "My Campsites",
            )
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
                    unselectedContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        when (selectedIndex) {
            0 -> OwnerCampsiteListScreen().CampsitesList(
                campsites = campsites,
                onAddCampsite = onAddCampsite,
                onEditCampsite = onEditCampsite
            )
//            1 -> LotteryContent()
//            2 -> ReservationsContent()
        }
    }
}
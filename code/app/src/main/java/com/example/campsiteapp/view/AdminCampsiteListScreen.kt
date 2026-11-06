/*
 * Admin list of all campsites. Tapping one opens its details.
 */
package com.example.campsiteapp.view

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.campsiteapp.model.Campsite
import com.example.campsiteapp.R
import com.example.campsiteapp.view.components.ScreenScaffold

/**
 * Screen listing every campsite.
 *
 * @param campsites campsites to show.
 * @param onRemoveCampsite called when the admin confirms removing a campsite.
 * @param onBack called when Back is pressed.
 */
@Composable
fun AdminCampsiteListScreen(
    campsites: List<Campsite>,
    onRemoveCampsite: (Campsite) -> Unit,
    onBack: () -> Unit,
) {
    var selectedId by rememberSaveable { mutableStateOf<String?>(null) }

    ScreenScaffold(title = stringResource(R.string.campsites_title), onBack = onBack) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(campsites, key = { it.id }) { campsite ->
                CampsiteRow(campsite, onClick = { selectedId = campsite.id })
            }
        }
    }

    campsites.find { it.id == selectedId }?.let { campsite ->
        AdminCampsiteDialog(
            campsite = campsite,
            onRemove = {
                onRemoveCampsite(campsite)
                selectedId = null
            },
            onDismiss = { selectedId = null },
        )
    }
}

@Composable
private fun CampsiteRow(campsite: Campsite, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(campsite.name, fontWeight = FontWeight.Bold)
            Text(campsite.location, color = Color.Gray)
            Text(stringResource(R.string.owned_by, campsite.owner))
            Text(stringResource(R.string.people, campsite.occupancy))
        }
        AsyncImage(
            model = campsite.image,
            contentDescription = stringResource(R.string.campsite_image),
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Crop,
        )
    }
}

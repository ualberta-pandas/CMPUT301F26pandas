/*
 * Popup showing all of a campsite's details, with a button to remove it.
 */
package com.example.campsiteapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.campsiteapp.model.Campsite
import com.example.campsiteapp.R
import com.example.campsiteapp.view.components.LabelledField
import com.example.campsiteapp.view.components.RemoveButton

/**
 * Campsite details popup.
 *
 * @param campsite campsite to show.
 * @param onRemove called when the admin confirms removing the campsite.
 * @param onDismiss called when the popup is closed.
 */
@Composable
fun AdminCampsiteDialog(campsite: Campsite, onRemove: () -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(campsite.name, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.close), color = Color.Gray)
                    }
                }
                AsyncImage(
                    model = campsite.image,
                    contentDescription = stringResource(R.string.campsite_image),
                    modifier = Modifier.fillMaxWidth().height(160.dp),
                    contentScale = ContentScale.Crop,
                )
                FieldRow(R.string.label_location, campsite.location, R.string.label_occupancy, campsite.occupancy)
                FieldRow(R.string.label_owner, campsite.owner, R.string.label_price, campsite.price)
                FieldRow(R.string.label_available_dates, campsite.adates, R.string.label_map_location, campsite.mapLocation)
                LabelledField(stringResource(R.string.label_description), campsite.description)
                LabelledField(stringResource(R.string.label_tags), campsite.tags)
                LabelledField(stringResource(R.string.label_rules), campsite.rules)
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    RemoveButton(
                        title = stringResource(R.string.remove_campsite_title),
                        message = stringResource(R.string.remove_campsite_message),
                        onConfirm = onRemove,
                    )
                }
            }
        }
    }
}

/**
 * Two labelled fields side by side.
 */
@Composable
private fun FieldRow(leftLabel: Int, leftValue: String, rightLabel: Int, rightValue: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        LabelledField(stringResource(leftLabel), leftValue, Modifier.weight(1f))
        LabelledField(stringResource(rightLabel), rightValue, Modifier.weight(1f))
    }
}

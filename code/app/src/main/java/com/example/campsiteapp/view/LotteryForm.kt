package com.example.campsiteapp.view


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.campsiteapp.helper.DateRangeField
import com.example.campsiteapp.model.Campsite
import com.example.campsiteapp.model.Lottery

@Composable
fun LotteryForm(
    initial: Lottery?,
    campsites: List<Campsite>,
    onSave: (Lottery) -> String?,
    onClose: () -> Unit,
) {
    var selectedIds by remember(initial) { mutableStateOf(initial?.campsiteIds?.toSet() ?: emptySet()) }
    var lotteryStart by remember(initial) { mutableStateOf(initial?.lotteryStartDate ?: "") }
    var lotteryEnd by remember(initial) { mutableStateOf(initial?.lotteryEndDate ?: "") }
    var bookingStart by remember(initial) { mutableStateOf(initial?.bookingStartDate ?: "") }
    var bookingEnd by remember(initial) { mutableStateOf(initial?.bookingEndDate ?: "") }
    var winners by remember(initial) { mutableStateOf((initial?.numWinners ?: 1).toString()) }
    var capacity by remember(initial) { mutableStateOf(initial?.waitlistMax?.toString() ?: "")}
    var error by remember { mutableStateOf<String?>(null) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                if(initial == null) {
                    "Create Lottery"
                } else {
                    "Edit Lottery"
                },
                style = MaterialTheme.typography.titleLarge
            )

            Text("Campsites in this lottery", fontWeight = FontWeight.Bold)
            campsites.forEach { campsite ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("campsiteCheckbox-${campsite.id}")
                        .clickable {
                            selectedIds = if(campsite.id in selectedIds) {
                                selectedIds - campsite.id
                            } else {
                                selectedIds + campsite.id
                            }
                        }
                ) {
                    Checkbox(checked = campsite.id in selectedIds, onCheckedChange = null)
                    Text(campsite.name, modifier = Modifier.padding(start = 8.dp))
                }
            }

            DateRangeField(
                label = "Lottery Period",
                start = lotteryStart,
                end = lotteryEnd,
                onChange = { start, end ->
                    lotteryStart = start
                    lotteryEnd = end
                },
                tag = "lottery"
            )

            DateRangeField(
                label = "Booking Period",
                start = bookingStart,
                end = bookingEnd,
                onChange = { start, end ->
                    bookingStart = start
                    bookingEnd = end
                },
                tag = "booking"
            )

            OutlinedTextField(
                value = winners,
                onValueChange = { winners = it.filter(Char::isDigit) },
                label = { Text("Number of Winners") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.testTag("winners")
            )

            OutlinedTextField(
                value = capacity,
                onValueChange = { capacity = it.filter(Char::isDigit) },
                label = { Text("Waitlist Capacity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.testTag("capacity")
            )

            error?.let {
                Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.testTag("lotteryError"))
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(
                    onClick = onClose,
                    modifier = Modifier.testTag("cancelLotteryButton")
                ) {
                    Text("Cancel")
                }

                Button(
                    modifier = Modifier.testTag("saveLotteryButton"),
                    onClick = {
                        val lottery = Lottery(
                            id = initial?.id ?: "",
                            ownerId = initial?.ownerId ?: campsites.firstOrNull { it.id in selectedIds }?.owner ?: "",
                            campsiteIds = selectedIds.toList(),
                            lotteryStartDate = lotteryStart,
                            lotteryEndDate = lotteryEnd,
                            bookingStartDate = bookingStart,
                            bookingEndDate = bookingEnd,
                            numWinners = winners.toIntOrNull() ?: 0,
                            waitlistMax = capacity.toIntOrNull() ?: 0,
                            status = initial?.status ?: ""
                        )
                        error = onSave(lottery)
                        if(error == null) {
                            onClose()
                        }
                    }
                ) {
                    Text("Save")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LotteryFormPreview() {
    LotteryForm(
        initial = null,
        campsites = listOf(
            Campsite(id = "1", name = "Campsite 1"),
            Campsite(id = "2", name = "Campsite 2"),
            Campsite(id = "3", name = "Campsite 3"),
        ),
        onSave = { null },
        onClose = {}
    )
}

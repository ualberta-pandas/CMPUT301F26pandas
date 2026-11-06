package com.example.campsiteapp.helper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * A labelled date range with a button that opens a range picker.
 *
 * Shows "Not set" until both dates are chosen. Picking a single day counts as
 * a one-day range.
 *
 * @param label what the range is for (Lottery, Booking, etc.)
 * @param start the range start as epoch millis at UTC midnight, or null if not set
 * @param end the range end as epoch millis at UTC midnight, or null if not set
 * @param onChange called with the new start and end when the owner confirms the picker
 * @param tag prefix for the test tag; the button is tagged "<tag>Button"
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangeField(
    label: String,
    start: Long?,
    end: Long?,
    onChange: (start: Long?, end: Long?) -> Unit,
    tag: String
) {
    var pickerOpen by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(label, fontWeight = FontWeight.Bold)
            Text(
                if (start == null || end == null) "Not set"
                else "${formatDate(start)} to ${formatDate(end)}"
            )
        }
        OutlinedButton(
            onClick = { pickerOpen = true },
            modifier = Modifier.testTag("${tag}Button")
        ) { Text("Choose") }
    }

    if (pickerOpen) {
        val pickerState = rememberDateRangePickerState(
            initialSelectedStartDateMillis = start,
            initialSelectedEndDateMillis = end
        )
        DatePickerDialog(
            onDismissRequest = { pickerOpen = false },
            confirmButton = {
                TextButton(
                    enabled = pickerState.selectedStartDateMillis != null,
                    onClick = {
                        val chosenStart = pickerState.selectedStartDateMillis
                        onChange(chosenStart, pickerState.selectedEndDateMillis ?: chosenStart)
                        pickerOpen = false
                    }
                ) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { pickerOpen = false }) { Text("Cancel") } }
        ) {
            DateRangePicker(state = pickerState, modifier = Modifier.height(500.dp))
        }
    }
}
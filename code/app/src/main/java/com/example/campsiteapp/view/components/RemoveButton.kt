/*
 * Red remove button and the confirmation dialog it opens, as in the admin mockups.
 */
package com.example.campsiteapp.view.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.campsiteapp.R

/**
 * Red trash button that asks for confirmation before calling [onConfirm].
 *
 * @param title title of the confirmation dialog.
 * @param message question shown in the confirmation dialog.
 * @param onConfirm called when the removal is confirmed.
 */
@Composable
fun RemoveButton(title: String, message: String, onConfirm: () -> Unit, modifier: Modifier = Modifier) {
    var confirming by rememberSaveable { mutableStateOf(false) }

    FilledIconButton(
        onClick = { confirming = true },
        modifier = modifier.size(36.dp).testTag("removeButton"),
        shape = CircleShape,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = Color.Red,
            contentColor = Color.White,
        ),
    ) {
        Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.remove))
    }

    if (confirming) {
        AlertDialog(
            onDismissRequest = { confirming = false },
            title = { Text(title) },
            text = { Text(message) },
            dismissButton = {
                Button(
                    onClick = { confirming = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                ) { Text(stringResource(R.string.cancel)) }
            },
            confirmButton = {
                Button(
                    onClick = {
                        confirming = false
                        onConfirm()
                    },
                    modifier = Modifier.testTag("confirmRemoveButton"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                ) { Text(stringResource(R.string.remove)) }
            },
        )
    }
}

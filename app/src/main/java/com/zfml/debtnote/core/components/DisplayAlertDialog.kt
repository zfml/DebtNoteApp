package com.zfml.debtnote.core.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@Composable
fun DisplayAlertDialog(
    title: String,
    description: String,
    openDialog: Boolean,
    onClosedDialog: () -> Unit,
    onConfirmClicked: () -> Unit,
    onDismissClicked: () -> Unit,
) {
    if (openDialog) {
        AlertDialog(
            title = {
                Text(
                    text = title,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = description,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
            },
            onDismissRequest = onDismissClicked,
            dismissButton = {
                OutlinedButton(onClick = onClosedDialog) {
                    Text(
                        text = "No"
                    )
                }
            },
            confirmButton = {
                Button(onClick = onConfirmClicked) {
                    Text(
                        text = "Yes"
                    )
                }
            }
        )
    }

}
package com.zfml.debtnote.presentation.debtList.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DebtHeader(
    headerName: String,
) {
    Column(
        modifier = Modifier
            .padding(8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = headerName,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DebtHeaderPreview(){
    DebtHeader(
        headerName = "Maung Hla",
    )
}
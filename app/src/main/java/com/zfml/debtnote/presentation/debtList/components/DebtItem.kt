package com.zfml.debtnote.presentation.debtList.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zfml.debtnote.core.components.DisplayAlertDialog
import com.zfml.debtnote.domain.model.Debt
import com.zfml.debtnote.util.toFormattedDateString

@Composable
fun DebtItem(
    debt: Debt,
    onDelete: (Int) -> Unit,
    onEdit: (Int) -> Unit,
) {
    val localDensity = LocalDensity.current
    var componentHeight by remember { mutableStateOf(0.dp) }




    var openDeleteDialog by remember { mutableStateOf(false) }


    DisplayAlertDialog(
        title = "Delete Debt!",
        description = "Are you sure you want to delete?",
        openDialog = openDeleteDialog,
        onClosedDialog = { openDeleteDialog = false},
        onConfirmClicked = {
            onDelete(debt.id ?: -1)
            openDeleteDialog = false
        }) {

    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp)
    ) {
        Spacer(
            modifier = Modifier.width(8.dp)
        )
        Surface(

            modifier = Modifier
                .width(2.dp)
                .height(componentHeight),
            tonalElevation = 1.dp,
            color = if (debt.oweMe) Color.Green else Color.Red
        ) {}
        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Surface(
            modifier = Modifier
                .clip(Shapes().small)
                .fillMaxWidth(),
            tonalElevation = 1.dp
        ) {
            Column(
                modifier = Modifier
                    .onGloballyPositioned {
                        componentHeight = with(localDensity) { it.size.height.toDp() }
                    }
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = debt.name,
                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = debt.debtDate.toFormattedDateString(),
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontWeight = FontWeight.Light
                    )

                }
                Text(
                    text = debt.amount.toString(),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = debt.description,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Light
                    )

                    Row(

                    ) {

                        IconButton(onClick = { onEdit(debt.id ?: -1)}) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Icon",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(onClick = { openDeleteDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Icon",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }


                    }

                }


            }
        }

    }
}

@Preview(showBackground = false)
@Composable
fun DebtItemPreview() {
    DebtItem(
        debt = Debt(
            id = 0,
            name = "Aung Ko Thein",
            debtDate = 0,
            amount = 9,
            description = "",
            oweMe = false
        ),
        onDelete = {

        },
        onEdit = {

        },
    )
}
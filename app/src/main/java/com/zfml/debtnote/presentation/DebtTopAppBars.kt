package com.zfml.debtnote.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.zfml.debtnote.R
import com.zfml.debtnote.util.toLong

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtsListTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onFilterByDate: () -> Unit,
    onFilterByName: () -> Unit,
    onFilterByDebtType: () -> Unit,
    onSearchClicked: () -> Unit
) {
    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = { Text(text = "Debts") },
        actions = {

            IconButton(onClick = onSearchClicked) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
            }
            FilterMenu(
                onFilterByName = onFilterByName,
                onFilterByDate = onFilterByDate,
                onFilterByDebtType = onFilterByDebtType
            )
        },
        modifier = Modifier.fillMaxWidth()
    )
}


@Composable
private fun FilterMenu(
  onFilterByName: () -> Unit,
  onFilterByDate:() -> Unit,
  onFilterByDebtType: () -> Unit
) {
    TopAppBarDropDownMenu(
        iconContent = {
            Icon(
                painter = painterResource(id = R.drawable.ic_filter_list),
                contentDescription = stringResource(id = R.string.icon_filter)
            )
        },
        content = {closeMenu ->
            DropdownMenuItem(text = { Text(text = "Name") }, onClick = { onFilterByName(); closeMenu()})
            DropdownMenuItem(text = { Text(text = "Date") }, onClick = { onFilterByDate(); closeMenu()})
            DropdownMenuItem(text = { Text(text = "Debt Type") }, onClick = { onFilterByDebtType(); closeMenu() })

        }
    )
}
@Composable
private fun TopAppBarDropDownMenu(
    iconContent: @Composable () -> Unit,
    content: @Composable ColumnScope.(() -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize(align = Alignment.TopEnd)) {
        IconButton(onClick = { expanded = !expanded}) {
            iconContent()
        }
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false}
    ) {
        content{expanded = !expanded}
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailDebtTopAppBar(
    onBack:() -> Unit
) {
    TopAppBar(
        title = { Text(text = "Detail Debt") },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back_icon)
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtListSearchTopAppBar(
    searchQuery: String,
    onSearchChanged:(String) -> Unit,
    onClear:() -> Unit
) {

    val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = Color.Transparent,
        unfocusedBorderColor = Color.Transparent,
    )


    TopAppBar(
        title = {
         OutlinedTextField(
             value = searchQuery ,
             onValueChange = onSearchChanged,
             placeholder = {
                Text(text = "Search With Name")
             },
             modifier = Modifier.fillMaxWidth(),
             leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
             },
             trailingIcon = {
                 IconButton(onClick = onClear) {
                     Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear Icon")
                 }
             },
             colors = textFieldColors
         )
        },

    )
}

@Preview
@Composable
fun DebtListSearchTopAppBarPreview() {
    DebtListSearchTopAppBar(
        onClear = {},
        onSearchChanged = {

        },
        searchQuery = ""
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditDebtTopAppBar(
    onBack: () -> Unit,
    onDateChange: (Long) -> Unit
) {
    TopAppBar(
        title = { Text(text = "Debt") },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back_icon)
                )
            }
        },
        actions = {
           DateMenu(onDateChange)
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateMenu(
    onDateChange: (Long) -> Unit
) {
    val dateDialogState = rememberSheetState()

    IconButton(onClick = {dateDialogState.show()}) {
        Icon(imageVector = Icons.Default.DateRange, contentDescription = stringResource(R.string.date_range_icon))
    }

    CalendarDialog(
        state = dateDialogState ,
        selection = CalendarSelection.Date{
            onDateChange(it.toLong())
            dateDialogState.show()
        },
        config = CalendarConfig(monthSelection = true, yearSelection = true)
    )




}
@Preview
@Composable
private fun AddEditDebtTopAppBarPreview() {
    AddEditDebtTopAppBar( onDateChange = {

    }, onBack = {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DebtsListTopAppBarPreview() {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    DebtsListTopAppBar(
        onSearchClicked = {

        },
        onFilterByDebtType = {

        },
        onFilterByName = {

        },
        onFilterByDate = {

        },
        scrollBehavior = scrollBehavior
    )
}
@Preview
@Composable
fun DebtDetailTopAppBarPreview() {
    DetailDebtTopAppBar(onBack = {})
}

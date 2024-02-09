package com.zfml.debtnote.presentation.debtList

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.zfml.debtnote.R
import com.zfml.debtnote.core.components.DisplayAlertDialog
import com.zfml.debtnote.domain.model.Debt
import com.zfml.debtnote.presentation.DebtsListTopAppBar
import com.zfml.debtnote.presentation.debtList.components.DebtHeader
import com.zfml.debtnote.presentation.debtList.components.DebtItem
import com.zfml.debtnote.presentation.debtList.components.DebtTotal
import com.zfml.debtnote.presentation.destinations.AddEditDebtScreenDestination
import com.zfml.debtnote.presentation.destinations.DebtListScreenDestination
import com.zfml.debtnote.presentation.destinations.DebtListSearchScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Destination(start = true)
@Composable
fun DebtListScreen(
    viewModel: DebtListViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            DebtsListTopAppBar(
                scrollBehavior = scrollBehavior,
                onFilterByDate = { viewModel.onEvent(DebtListEvent.FilterByDate) },
                onFilterByName = { viewModel.onEvent(DebtListEvent.FilterByName) },
                onFilterByDebtType = { viewModel.onEvent(DebtListEvent.FilterByDebtType)},
                onSearchClicked = { navigator.navigate(DebtListSearchScreenDestination())}
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .clip(CircleShape)
                ,
                onClick = {
                navigator.navigate(AddEditDebtScreenDestination(-1))
            }) {
               Icon(
                   imageVector = Icons.Default.Add,
                   contentDescription = stringResource(R.string.add_icon)
               )
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier.padding(paddingValues = padding)
            ) {
                DebtListContent(
                    uiState = uiState,
                    onEdit = {navigator.navigate(AddEditDebtScreenDestination(it))},
                    onDelete = {viewModel.onEvent(DebtListEvent.DeleteDebt(it))}
                )
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DebtListContent(
    uiState: DebtListUiState,
    onDelete: (Int) -> Unit,
    onEdit: (Int) -> Unit,
) {




    if (uiState.debts.isNotEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn() {
                uiState.debts.forEach { key, debts ->
                    stickyHeader(key = key) {
                        DebtHeader(headerName = key)
                    }
                    item {
                        DebtTotal(debts = debts)
                    }
                    items(
                        items = debts,
                    ) { debt ->
                        DebtItem(
                            debt = debt,
                            onDelete = onDelete,
                            onEdit = onEdit
                        )
                    }
                }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No Debt")
        }
    }

}

val list = mutableListOf<Debt>(
    Debt(
        id = 0,
        name = "Zun Ko Win",
        description = "",
        oweMe = false,
        amount = 35000,
        debtDate = 89
    ),
    Debt(
        id = 1,
        name = "Aung Ko Thein",
        description = "",
        oweMe = true,
        amount = 45000,
        debtDate = 89
    ),
    Debt(
        id = 2,
        name = "Aye Min Aung",
        description = "",
        oweMe = false,
        amount = 35000,
        debtDate = 90
    ),
    Debt(
        id = 3,
        name = "Thu Kha Min Soe",
        description = "",
        oweMe = false,
        amount = 35000,
        debtDate = 90
    ),
    Debt(
        id = 4,
        name = "Aung Ko Thein",
        description = "",
        oweMe = false,
        amount = 45000,
        debtDate = 56
    ),
    Debt(
        id = 5,
        name = "Aung Ko Thein",
        description = "",
        oweMe = true,
        amount = 45000,
        debtDate = 89
    ),
)

@Preview(showBackground = true)
@Composable
fun DebtListContentPreview() {


    DebtListContent(
        uiState = DebtListUiState(
            debts = list.groupBy { it.name }
        ),
        onDelete = {

        },
        onEdit = {

        },
    )
}
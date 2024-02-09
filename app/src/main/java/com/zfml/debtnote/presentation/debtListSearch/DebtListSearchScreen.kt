package com.zfml.debtnote.presentation.debtListSearch

import com.zfml.debtnote.presentation.DebtListSearchTopAppBar
import com.zfml.debtnote.presentation.debtList.DebtListContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.zfml.debtnote.presentation.destinations.AddEditDebtScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun DebtListSearchScreen(
    viewModel: DebtListSearchViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            DebtListSearchTopAppBar(
                searchQuery = searchQuery,
                onSearchChanged = {viewModel.onEvent(DebtListSearchEvent.Search(it))},
                onClear = { navigator.popBackStack() }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier.padding(paddingValues = padding)
            ) {
                DebtListContent(
                    uiState = uiState,
                    onEdit = {navigator.navigate(AddEditDebtScreenDestination(it))},
                    onDelete = {viewModel.onEvent(DebtListSearchEvent.DeleteDebt(it))}
                )
            }
        }
    )
}




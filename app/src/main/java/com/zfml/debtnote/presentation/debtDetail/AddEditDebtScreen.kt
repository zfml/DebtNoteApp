package com.zfml.debtnote.presentation.debtDetail

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.zfml.debtnote.R
import com.zfml.debtnote.presentation.AddEditDebtTopAppBar
import com.zfml.debtnote.presentation.DetailDebtTopAppBar
import com.zfml.debtnote.util.DebtType
import com.zfml.debtnote.util.toFormattedDateString
import kotlinx.coroutines.flow.collectLatest

@Destination
@Composable
fun AddEditDebtScreen(
    navigator: DestinationsNavigator,
    id: Int,
) {
    val viewModel: AddEditDebtViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsState()
    val filteredNames by viewModel.filteredNamesList.collectAsState()

    val context = LocalContext.current


    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest {
            when(it) {
                is AddEditDebtUiEvent.ErrorMessage -> {
                    Toast.makeText(
                        context,
                        it.errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                AddEditDebtUiEvent.Save -> {
                    navigator.popBackStack()
                }
            }
        }
    }



    Scaffold(
        topBar = {
            AddEditDebtTopAppBar(
                onDateChange = { viewModel.onEvent(AddEditDebtEvent.DebtDateChange(it)) },
                onBack = {
                    navigator.popBackStack()
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditDebtEvent.Save)
                }) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = stringResource(R.string.done_icon)
                )
            }
        },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                AddEditDebtContent(
                    context = context,
                    nameList = filteredNames,
                    uiState = uiState,
                    onNameChanged = {
                        viewModel.onEvent(AddEditDebtEvent.NameChange(it))
                        viewModel.onEvent(AddEditDebtEvent.SearchName(it))
                    },
                    onSearchClicked = {
                        viewModel.onEvent(AddEditDebtEvent.NameChange(it))
                        viewModel.onEvent(AddEditDebtEvent.SearchName(""))
                    },
                    onFocusChanged = {
                        if (!it.isFocused) {
                            viewModel.onEvent(AddEditDebtEvent.SearchName(""))
                        }
                    },
                    onAmountChanged = { viewModel.onEvent(AddEditDebtEvent.AmountChange(it)) },
                    onDescriptionChanged = { viewModel.onEvent(AddEditDebtEvent.DescriptionChange(it)) },
                    onDebtTypeChange = { viewModel.onEvent(AddEditDebtEvent.DebtTypeChange(it)) }
                )
            }
        }
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditDebtContent(
    context: Context,
    nameList: List<String>,
    uiState: AddEditDebtUiState,
    onNameChanged: (String) -> Unit,
    onSearchClicked: (String) -> Unit,
    onFocusChanged: (FocusState) -> Unit,
    onAmountChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onDebtTypeChange: (DebtType) -> Unit,
) {

    val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = Color.Transparent,
        unfocusedBorderColor = Color.Transparent,
    )

    LaunchedEffect(key1 = uiState.errorMessage) {
        if (uiState.errorMessage != "") {
            Toast.makeText(
                context,
                uiState.errorMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            modifier = Modifier
                .padding(8.dp),
            text = uiState.debtDate.toFormattedDateString()
        )

        SearchableDropDownTextField(
            nameList = nameList,
            name = uiState.name,
            colors = textFieldColors,
            onNameChanged = onNameChanged,
            onSearchClicked = onSearchClicked,
            onFocusChanged = onFocusChanged
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.amount,
            onValueChange = {
                onAmountChanged(it)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = {
                Text(text = stringResource(id = R.string.place_holder_amount))
            },
            colors = textFieldColors
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.description,
            onValueChange = onDescriptionChanged,
            placeholder = {
                Text(text = stringResource(id = R.string.place_holder_description))
            },
            colors = textFieldColors
        )

        DebtTypeSection(
            debtType = if (uiState.oweMe) DebtType.GET else DebtType.PAY,
            onSelected = onDebtTypeChange
        )


    }
}

@Composable
fun DebtTypeSection(
    debtType: DebtType,
    onSelected: (DebtType) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        CustomRadioButton(
            debtType = DebtType.GET,
            selected = debtType.name == DebtType.GET.name,
            onSelected = onSelected
        )
        CustomRadioButton(
            debtType = DebtType.PAY,
            selected = debtType.name == DebtType.PAY.name,
            onSelected = onSelected
        )
    }
}


@Composable
fun CustomRadioButton(
    debtType: DebtType,
    selected: Boolean,
    onSelected: (DebtType) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = debtType.name
        )
        RadioButton(
            selected = selected,
            onClick = { onSelected(debtType) }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchableDropDownTextField(
    nameList: List<String>,
    name: String,
    colors: TextFieldColors,
    onNameChanged: (String) -> Unit,
    onSearchClicked: (String) -> Unit,
    onFocusChanged: (FocusState) -> Unit,
) {

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        OutlinedTextField(
            value = name,
            onValueChange = onNameChanged,
            placeholder = {
                Text(text = stringResource(id = R.string.place_holder_name))
            },
            colors = colors,
            modifier = Modifier.onFocusChanged {
                onFocusChanged(it)
            }
        )


        AnimatedVisibility(visible = nameList.isNotEmpty()) {
            LazyColumn() {
                items(nameList) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .clickable {
                                onSearchClicked(it)
                            },
                        tonalElevation = 1.dp
                    ) {

                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = it
                        )
                    }
                }
            }
        }


    }
}


@Preview(showBackground = true)
@Composable
fun CustomRadioButtonPreview() {
    CustomRadioButton(debtType = DebtType.GET, selected = true, onSelected = {

    })
}

@Preview(showBackground = true)
@Composable
fun AddEditDebtContentPreview() {
    AddEditDebtContent(
        context = LocalContext.current,
        nameList = emptyList(),
        uiState = AddEditDebtUiState(),
        onNameChanged = {

        },
        onAmountChanged = {

        },
        onDescriptionChanged = {

        },
        onDebtTypeChange = {

        },
        onSearchClicked = {

        },
        onFocusChanged = {

        }
    )
}

package com.zfml.debtnote.presentation.debtList

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zfml.debtnote.domain.model.Debt
import com.zfml.debtnote.domain.repository.DebtRepository
import com.zfml.debtnote.util.FilterType
import com.zfml.debtnote.util.toDebtType
import com.zfml.debtnote.util.toFormattedDateString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DebtListViewModel @Inject constructor(
    private val debtRepository: DebtRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _filterType = savedStateHandle.getStateFlow(FILTER_STATE_KEY, FilterType.NAME)
    

    val uiState = combine(debtRepository.getAllDebtsStream(),_filterType) {
        tasksStream , filterType ->

            when(filterType) {
                FilterType.NAME -> {
                     tasksStream.groupBy {
                        it.name
                    }
                }
                FilterType.DATE ->  {
                    tasksStream.groupBy {
                        it.debtDate.toFormattedDateString()
                    }
                }

                FilterType.DEBT_TYPE -> {
                    tasksStream.groupBy {
                        it.oweMe.toDebtType()
                    }
                }
            }

    }.map {
        if(it.isNotEmpty()){
            DebtListUiState(
                debts = it
            )
        } else {
            DebtListUiState(
                emptyMessage = "No Debts"
            )
        }
    }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L),DebtListUiState())

    fun onEvent(event: DebtListEvent) {
        when(event) {
            DebtListEvent.FilterByDate -> {
                setFilterType(FilterType.DATE)
            }
            DebtListEvent.FilterByName -> {
                setFilterType(FilterType.NAME)
            }
            DebtListEvent.FilterByDebtType -> {
                setFilterType(FilterType.DEBT_TYPE)
            }
            is DebtListEvent.DeleteDebt -> {
                deleteDebt(event.debtId)
            }

        }

    }

    private fun deleteDebt(debtId: Int) {
        viewModelScope.launch {
            debtRepository.deleteDebt(debtId)
        }
    }

    private fun setFilterType(type: FilterType) {
        savedStateHandle[FILTER_STATE_KEY] = type
    }


}

data class DebtListUiState(
    val debts: Map<String,List<Debt>> = emptyMap(),
    val userMessage: Int? = null,
    val emptyMessage: String = "",
)
const val FILTER_STATE_KEY = "FILTER_STATE_KEY"
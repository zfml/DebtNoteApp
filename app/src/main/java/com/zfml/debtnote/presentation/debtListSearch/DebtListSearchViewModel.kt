package com.zfml.debtnote.presentation.debtListSearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zfml.debtnote.domain.model.Debt
import com.zfml.debtnote.domain.repository.DebtRepository
import com.zfml.debtnote.presentation.debtList.DebtListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DebtListSearchViewModel @Inject constructor(
    private val debtRepository: DebtRepository
): ViewModel() {
     val searchQuery = MutableStateFlow("")

    val uiState = combine(searchQuery,debtRepository.getAllDebtsStream()) { searchQuery , debtList ->

        val filterDebts = filterBySearch(searchQuery,debtList)
        if(filterDebts.isNotEmpty()) {
            DebtListUiState(
                debts =  filterDebts,
            )
        } else {
            DebtListUiState(
                emptyMessage = "There is no such name"
            )
        }

    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DebtListUiState()
    )


    fun onEvent(event: DebtListSearchEvent) {
        when(event) {
            is DebtListSearchEvent.DeleteDebt -> {
                deleteDebt(event.debtId)
            }
            is DebtListSearchEvent.Search -> {
                updateSearchQuery(event.searchQuery)
            }
        }
    }

    private fun deleteDebt(debtId: Int) {
        viewModelScope.launch {
            debtRepository.deleteDebt(debtId)
        }
    }
    private fun updateSearchQuery(search: String) {
        searchQuery.value = search
    }


    private fun filterBySearch(searchQuery: String, debtList: List<Debt>): Map<String,List<Debt>> {
      return  debtList.filter { it.name.contains(searchQuery, ignoreCase = true) }.groupBy { it.name }
    }

}

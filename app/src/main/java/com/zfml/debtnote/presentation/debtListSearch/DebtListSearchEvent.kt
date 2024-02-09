package com.zfml.debtnote.presentation.debtListSearch

import com.zfml.debtnote.domain.model.Debt

sealed class DebtListSearchEvent {
    data class Search(val searchQuery: String): DebtListSearchEvent()
    data class DeleteDebt(val debtId: Int): DebtListSearchEvent()
}
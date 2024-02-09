package com.zfml.debtnote.presentation.debtList

sealed class DebtListEvent {
    data object FilterByName: DebtListEvent()
    data object FilterByDate: DebtListEvent()
    data object FilterByDebtType: DebtListEvent()
    data class DeleteDebt(val debtId: Int): DebtListEvent()
}
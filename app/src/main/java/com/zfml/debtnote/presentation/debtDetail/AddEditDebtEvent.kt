package com.zfml.debtnote.presentation.debtDetail

import com.zfml.debtnote.util.DebtType

sealed class AddEditDebtEvent {
    data object  Save: AddEditDebtEvent()
    data class NameChange(val name: String): AddEditDebtEvent()
    data class AmountChange(val amount: String): AddEditDebtEvent()
    data class DescriptionChange(val description: String): AddEditDebtEvent()
    data class DebtTypeChange(val type: DebtType): AddEditDebtEvent()
    data class DebtDateChange(val date: Long): AddEditDebtEvent()
    data class SearchName(val queryName: String): AddEditDebtEvent()
}
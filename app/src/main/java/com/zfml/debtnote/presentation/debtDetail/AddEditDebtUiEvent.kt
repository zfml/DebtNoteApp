package com.zfml.debtnote.presentation.debtDetail

sealed class AddEditDebtUiEvent {
    data object Save: AddEditDebtUiEvent()
    data class ErrorMessage(val errorMessage: String): AddEditDebtUiEvent()
}
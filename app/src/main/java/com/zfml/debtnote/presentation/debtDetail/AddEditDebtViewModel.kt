package com.zfml.debtnote.presentation.debtDetail

import androidx.fragment.app.strictmode.SetRetainInstanceUsageViolation
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zfml.debtnote.domain.model.Debt
import com.zfml.debtnote.domain.model.InvalidDebtNoteException
import com.zfml.debtnote.domain.repository.DebtRepository
import com.zfml.debtnote.util.DebtType
import com.zfml.debtnote.util.toLong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class AddEditDebtUiState(
    val nameList: List<String> = emptyList(),
    val name: String = "",
    val amount: String  = "",
    val description: String = "",
    val debtDate: Long = LocalDate.now().toLong(),
    val oweMe: Boolean = false,
    val errorMessage: String = "",
    val isValidInput: Boolean  = false
)

@HiltViewModel
class AddEditDebtViewModel @Inject constructor(
    private val debtRepository: DebtRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {


    private val _searchQuery = MutableStateFlow("")

    val filteredNamesList = combine(_searchQuery, debtRepository.getAllNamesStream()) { searchName , names ->

        if(searchName.isEmpty()) {
            emptyList()
        }else {
            names.filter { it.contains(searchName, ignoreCase = true) }.distinct()
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _uiState = MutableStateFlow(AddEditDebtUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<AddEditDebtUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    private var currentId: Int? = null




    init {
        viewModelScope.launch {
            savedStateHandle.get<Int>("id")?.let {debtId ->
                if(debtId != -1) {
                    debtRepository.getDebt(debtId).let {

                        currentId = it.id
                        updateName(it.name)
                        updateAmount(it.amount.toString())
                        updateDate(it.debtDate)
                        if(it.oweMe) {
                            updateDebtType(DebtType.GET)
                        } else {
                            updateDebtType(DebtType.PAY)
                        }
                        updateDescription(it.description)
                    }
                }

            }
        }
    }







    fun onEvent(event: AddEditDebtEvent) {
        when(event) {
            is AddEditDebtEvent.AmountChange -> {
                updateAmount(event.amount)
            }
            is AddEditDebtEvent.DebtDateChange -> {
                updateDate(event.date)
            }
            is AddEditDebtEvent.DebtTypeChange -> {
                updateDebtType(event.type)
            }
            is AddEditDebtEvent.DescriptionChange -> {
                updateDescription(event.description)
            }
            is AddEditDebtEvent.NameChange -> {
                updateName(event.name)
            }
            AddEditDebtEvent.Save -> {
                saveHandle()
            }

            is AddEditDebtEvent.SearchName -> {
                updateSearchQuery(event.queryName)
            }
        }
    }



    private fun updateSearchQuery(searchQuery: String) {
        _searchQuery.value = searchQuery
    }


    private fun updateNameList() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    nameList = debtRepository.getAllNames()
                )
            }
        }
    }

    private fun saveHandle(
        name: String = _uiState.value.name,
        amount: String = _uiState.value.amount,
        debtDate: Long = _uiState.value.debtDate,
        oweMe: Boolean = _uiState.value.oweMe
    ) {
        if(name.isEmpty()) {
            updateErrorMessage("Name is Empty.")
            updateValidInput(false)
            return
        }

       if(amount.isEmpty()) {
           updateErrorMessage("Amount is Empty")
           updateValidInput(false)
           return
       }

        if(amount.toIntOrNull() == null) {
            updateErrorMessage(" Enter Invalid Amount  ")
            updateValidInput(false)
            return
        }

        updateValidInput(true)
        if(currentId != null) {
            update(currentId!!)
        } else{
            save()

        }



    }

    private fun save() {
        viewModelScope.launch {
            try {
                debtRepository.insertDebt(Debt(
                    name = _uiState.value.name,
                    amount = _uiState.value.amount.toIntOrNull() ?: 0,
                    debtDate = _uiState.value.debtDate,
                    description = _uiState.value.description,
                    oweMe = _uiState.value.oweMe
                ))
            } catch (e: InvalidDebtNoteException) {
                _eventFlow.emit(AddEditDebtUiEvent.ErrorMessage(e.message.toString()))
            }
            _eventFlow.emit(AddEditDebtUiEvent.Save)
        }
    }

    private fun update(currentId: Int) {
        viewModelScope.launch {
            try {
                debtRepository.insertDebt(Debt(
                    id = currentId,
                    name = _uiState.value.name,
                    amount = _uiState.value.amount.toIntOrNull() ?: 0,
                    debtDate = _uiState.value.debtDate,
                    description = _uiState.value.description,
                    oweMe = _uiState.value.oweMe
                ))
                _eventFlow.emit(AddEditDebtUiEvent.Save)
            }catch (e: InvalidDebtNoteException) {
                _eventFlow.emit(AddEditDebtUiEvent.ErrorMessage(e.message.toString()))
            }


        }
    }



    private fun updateValidInput(isValidInput: Boolean) {
         _uiState.update {
             it.copy(
                 isValidInput = isValidInput
             )
         }
    }

    private fun updateErrorMessage(message: String) {
        _uiState.update {
            it.copy(
                errorMessage = message
            )
        }
    }

    private fun updateName(name: String) {
        _uiState.update {
            it.copy(
                name  = name
            )
        }
    }

    private fun updateAmount(amount: String) {
        _uiState.update {
            it.copy(
                amount = amount
            )
        }
    }

    private fun updateDescription(description: String) {
        _uiState.update {
            it.copy(
                description = description
            )
        }
    }

    private fun updateDebtType(type: DebtType) {
        _uiState.update {
            if(type == DebtType.PAY) {
                it.copy(
                    oweMe = false
                )
            } else{
                it.copy(
                    oweMe = true
                )
            }

        }
    }

    private fun updateDate(date: Long) {
        _uiState.update {
            it.copy(
                debtDate = date
            )
        }
    }




}
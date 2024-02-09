package com.zfml.debtnote.domain.model

import androidx.room.PrimaryKey

data class Debt(
    val id: Int? = null,
    val name: String,
    val oweMe: Boolean,
    val amount: Int,
    val debtDate: Long,
    val description: String = ""
)

class InvalidDebtNoteException(message: String): Exception(message)
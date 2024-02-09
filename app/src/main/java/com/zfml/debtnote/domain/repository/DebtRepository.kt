package com.zfml.debtnote.domain.repository

import com.zfml.debtnote.domain.model.Debt
import kotlinx.coroutines.flow.Flow


interface DebtRepository {

    fun getAllDebtsStream(): Flow<List<Debt>>

    fun getDebtStream(debtId: Int): Flow<Debt>

    suspend fun getAllDebts(): List<Debt>

    suspend fun getDebt(debtId: Int): Debt

    suspend fun insertDebt(debt: Debt)

    suspend fun deleteDebt(debtId: Int)

    suspend fun deleteAll()

    fun getAllNamesStream(): Flow<List<String>>

    suspend fun getAllNames(): List<String>


}
package com.zfml.debtnote.data.repository

import com.zfml.debtnote.data.local.DebtDao
import com.zfml.debtnote.data.mappers.toDebt
import com.zfml.debtnote.data.mappers.toDebtsList
import com.zfml.debtnote.data.mappers.toLocalDebt
import com.zfml.debtnote.domain.model.Debt
import com.zfml.debtnote.domain.repository.DebtRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DebtRepositoryImpl @Inject constructor(
    private val localDataSource: DebtDao
): DebtRepository {
    override fun getAllDebtsStream(): Flow<List<Debt>> {
        return localDataSource.getAllDebtsStream().map { it.toDebtsList() }
    }

    override fun getDebtStream(debtId: Int): Flow<Debt> {
        return localDataSource.getDebtByIdStream(debtId).map { it.toDebt() }
    }

    override suspend fun getAllDebts(): List<Debt> {
        return localDataSource.getAllDebts().toDebtsList()
    }

    override suspend fun getDebt(debtId: Int): Debt {
        return localDataSource.getDebtById(debtId).toDebt()
    }

    override suspend fun insertDebt(debt: Debt) {
        localDataSource.upsert(debt.toLocalDebt())
    }

    override suspend fun deleteDebt(debtId: Int) {
         localDataSource.deleteDebt(debtId)
    }

    override suspend fun deleteAll() {
        localDataSource.deleteAll()
    }

    override fun getAllNamesStream(): Flow<List<String>> {
       return localDataSource.getNamesStream()
    }

    override suspend fun getAllNames(): List<String> {
        return localDataSource.getAllNames()
    }
}
package com.zfml.debtnote.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface DebtDao {

    @Query("SELECT * FROM debt")
    fun getAllDebtsStream(): Flow<List<LocalDebt>>

    @Query("SELECT * FROM debt WHERE id =:debtId")
    fun getDebtByIdStream(debtId: Int): Flow<LocalDebt>

    @Query("SELECT * FROM debt")
    suspend fun getAllDebts(): List<LocalDebt>

    @Query("SELECT * FROM debt WHERE id = :debtId")
    suspend fun getDebtById(debtId: Int): LocalDebt

    @Query("SELECT name FROM debt")
    fun getNamesStream(): Flow<List<String>>

    @Query("SELECT name FROM debt")
    suspend fun getAllNames(): List<String>

    @Upsert
    suspend fun upsert(debt: LocalDebt)

    @Query("DELETE FROM debt WHERE id = :debtId")
    suspend fun deleteDebt(debtId: Int)

    @Query("DELETE FROM debt")
    suspend fun deleteAll()


}
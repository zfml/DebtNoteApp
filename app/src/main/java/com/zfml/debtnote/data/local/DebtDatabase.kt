package com.zfml.debtnote.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [LocalDebt::class],
    version = 1
)
abstract class DebtDatabase: RoomDatabase() {
    abstract val debtDao: DebtDao
}
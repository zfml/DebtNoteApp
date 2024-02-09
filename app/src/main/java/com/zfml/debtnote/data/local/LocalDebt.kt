package com.zfml.debtnote.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "debt")
data class LocalDebt(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val oweMe: Boolean,
    val amount: Int,
    val debtDate: Long,
    val description: String
)

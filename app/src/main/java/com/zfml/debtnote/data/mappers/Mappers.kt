package com.zfml.debtnote.data.mappers

import com.zfml.debtnote.data.local.LocalDebt
import com.zfml.debtnote.domain.model.Debt

fun LocalDebt.toDebt(): Debt = Debt(
    id = id,
    name = name,
    oweMe = oweMe,
    amount = amount,
    debtDate = debtDate,
    description = description
)

fun Debt.toLocalDebt(): LocalDebt = LocalDebt(
    id = id,
    name = name,
    oweMe = oweMe,
    amount = amount,
    debtDate = debtDate,
    description = description
)

fun List<Debt>.toLocalDebtList(): List<LocalDebt> = map(Debt::toLocalDebt)

fun List<LocalDebt>.toDebtsList(): List<Debt> = map(LocalDebt::toDebt)

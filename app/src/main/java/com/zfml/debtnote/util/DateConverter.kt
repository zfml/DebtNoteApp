package com.zfml.debtnote.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date

fun Long.toLocalDate(): LocalDate {
    return Date(this).toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

fun Long.toLocalDateTime(): LocalDateTime {
    return Date(this).toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
}

fun dateTimeFormatter(date: LocalDate): String{
   return  DateTimeFormatter
        .ofPattern("dd MMM yyyy")
        .format(date).uppercase()
}

fun Long.toFormattedDateString(): String{
    val localDate =  this.toLocalDate()
    return dateTimeFormatter(localDate)
}

fun LocalDate.toLong(): Long {
    return this.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
}
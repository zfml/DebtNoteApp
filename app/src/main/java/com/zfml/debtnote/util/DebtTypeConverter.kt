package com.zfml.debtnote.util

fun Boolean.toDebtType(): String {
    return if(this){
        "Total to Get"
    } else {
        "Total to Pay"
    }
}
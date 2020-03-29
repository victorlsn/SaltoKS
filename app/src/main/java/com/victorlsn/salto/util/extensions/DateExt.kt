package com.victorlsn.salto.util.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Date.getStringInFormat(format: String): String {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return dateFormat.format(this)
}
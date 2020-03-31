package com.victorlsn.salto.util.extensions

import android.content.SharedPreferences

inline fun SharedPreferences.putString(key: String, value: String) {
    val editor = this.edit()

    editor.putString(key, value)

    editor.apply()
}
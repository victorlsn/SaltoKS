package com.victorlsn.salto.util

import android.content.Context
import android.widget.Toast

class ToastHelper {
    private var currentToast: Toast? = null

    fun showToast(context: Context?, content: String) {
        currentToast?.cancel()

        currentToast = Toast.makeText(context!!, content, Toast.LENGTH_SHORT)
        currentToast?.show()
    }
}
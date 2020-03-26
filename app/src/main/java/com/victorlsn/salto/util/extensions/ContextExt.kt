package com.victorlsn.salto.util.extensions

import android.content.Context
import com.kaopiz.kprogresshud.KProgressHUD

fun Context.getLoading(): KProgressHUD {
    return KProgressHUD.create(this)
        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
        .setLabel("Please wait")
        .setCancellable(true)
        .setAnimationSpeed(2)
        .setDimAmount(0.5f)
}
package com.victorlsn.salto.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import com.kaopiz.kprogresshud.KProgressHUD
import com.victorlsn.salto.R
import com.victorlsn.salto.ui.fragments.BaseFragment
import com.victorlsn.salto.util.ToastHelper
import com.victorlsn.salto.util.extensions.getLoading
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

@SuppressLint("Registered")
open class BaseActivity : DaggerAppCompatActivity() {
    private var doubleBackToExitPressedOnce = false
    var activeFragment: BaseFragment? = null
    open lateinit var loading: KProgressHUD

    @Inject
    lateinit var toastHelper: ToastHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        loading = getLoading()
        super.onCreate(savedInstanceState)
    }

    override fun onBackPressed() {
        var handled = false
        activeFragment?.let {
            if (activeFragment!!.isAdded) {
                handled = it.onBackPressed()
            }
        }
        if (!handled) {
            if (!doubleBackToExitPressedOnce) {
                doubleBackToExitPressedOnce = true
                toastHelper.showToast(this, getString(R.string.press_back_again))
                Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
            } else {
                super.onBackPressed()
            }
        }
    }
}
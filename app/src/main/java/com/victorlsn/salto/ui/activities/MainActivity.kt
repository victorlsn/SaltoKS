package com.victorlsn.salto.ui.activities

import android.os.Bundle
import com.victorlsn.salto.R

class MainActivity :
    BaseActivity() {
    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
package com.victorlsn.salto.application

import android.content.ContextWrapper
import com.pixplicity.easyprefs.library.Prefs
import com.victorlsn.salto.di.components.AppComponent
import com.victorlsn.salto.BuildConfig
import com.victorlsn.salto.di.components.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class App : DaggerApplication() {
    var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            // init Timber
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        initializePrefs()

        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()

        return appComponent as AndroidInjector<out DaggerApplication>
    }

    private fun initializePrefs() {
        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()
    }

}
package com.victorlsn.salto.application

import android.content.ContextWrapper
import com.victorlsn.salto.BuildConfig
import com.victorlsn.salto.di.components.AppComponent
import com.victorlsn.salto.di.components.DaggerAppComponent
import com.victorlsn.salto.di.modules.HelperModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class App : DaggerApplication() {
    private var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            // init Timber
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent = DaggerAppComponent.builder()
            .helperModule(HelperModule())
            .application(this)
            .build()

        return appComponent as AndroidInjector<out DaggerApplication>
    }

}
package com.victorlsn.salto.di.components

import android.app.Application
import com.victorlsn.salto.application.App
import com.victorlsn.salto.di.modules.ActivityBindingModule
import com.victorlsn.salto.di.modules.ApplicationModule
import com.victorlsn.salto.di.modules.HelperModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        ActivityBindingModule::class,
        HelperModule::class,
        AndroidSupportInjectionModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun helperModule(helperModule: HelperModule): Builder

        fun build(): AppComponent
    }
}
package com.victorlsn.salto.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.victorlsn.salto.data.Repository
import com.victorlsn.salto.util.BaseSchedulerProvider
import com.victorlsn.salto.util.SchedulerProvider
import com.victorlsn.salto.util.ToastHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HelperModule {
    @Singleton
    @Provides
    fun provideToastHelper(): ToastHelper {
        return ToastHelper()
    }

    @Singleton
    @Provides
    fun provideSchedulerProvider(): BaseSchedulerProvider {
        return SchedulerProvider()
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideRepository(sharedPreferences: SharedPreferences): Repository {
        return Repository(sharedPreferences)
    }
}
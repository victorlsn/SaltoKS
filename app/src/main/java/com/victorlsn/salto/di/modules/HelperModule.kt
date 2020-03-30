package com.victorlsn.salto.di.modules

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
}
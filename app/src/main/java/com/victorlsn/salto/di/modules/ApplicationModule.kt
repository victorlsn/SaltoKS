package com.victorlsn.salto.di.modules

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class ApplicationModule {

	@Binds
	internal abstract fun bindContext(application: Application): Context
}
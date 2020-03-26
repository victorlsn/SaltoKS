package com.victorlsn.salto.di.modules

import com.victorlsn.salto.di.scopes.ActivityScoped
import com.victorlsn.salto.ui.activities.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

	@ActivityScoped
	@ContributesAndroidInjector(modules = [FragmentBindingModule::class])
	internal abstract fun mainActivity(): MainActivity

}

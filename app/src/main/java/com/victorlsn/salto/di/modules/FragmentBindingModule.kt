package com.victorlsn.salto.di.modules

import com.victorlsn.salto.di.scopes.FragmentScoped
import com.victorlsn.salto.ui.fragments.*
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentBindingModule {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun usersFragment(): UsersFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun doorsFragment(): DoorsFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun accessFragment(): AccessFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun openDoorFragment(): OpenDoorFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun eventsFragment(): EventsFragment

}
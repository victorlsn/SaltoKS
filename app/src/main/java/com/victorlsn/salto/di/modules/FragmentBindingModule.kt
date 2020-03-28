package com.victorlsn.salto.di.modules

import com.victorlsn.salto.di.scopes.FragmentScoped
import com.victorlsn.salto.ui.fragments.AccessFragment
import com.victorlsn.salto.ui.fragments.DoorsFragment
import com.victorlsn.salto.ui.fragments.EmployeesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentBindingModule {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun employeesFragment(): EmployeesFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun doorsFragment(): DoorsFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun accessFragment(): AccessFragment

}
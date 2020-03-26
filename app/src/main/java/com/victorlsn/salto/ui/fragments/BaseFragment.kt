package com.victorlsn.salto.ui.fragments

import android.os.Bundle
import com.kaopiz.kprogresshud.KProgressHUD
import com.victorlsn.salto.ui.activities.BaseActivity
import dagger.android.support.DaggerFragment

abstract class BaseFragment : DaggerFragment() {
    open lateinit var loading: KProgressHUD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loading = (activity as BaseActivity).loading
    }

    open fun resumeFragment() {}

    open fun onBackPressed(): Boolean {
        if (childFragmentManager.fragments.size > 0) {
            if ((childFragmentManager.fragments.last() as? BaseFragment) != null) {
                if (!(childFragmentManager.fragments.last() as? BaseFragment)!!.onBackPressed()) {
                    childFragmentManager.beginTransaction().remove(childFragmentManager.fragments.last()).commitNow()
                }
            }
            else {
                childFragmentManager.beginTransaction().remove(childFragmentManager.fragments.last()).commitNow()
            }

            if (childFragmentManager.fragments.size > 0) {
                (childFragmentManager.fragments.last() as? BaseFragment)?.resumeFragment()
            }
            else {
                resumeFragment()
            }


            return true
        }

        return false
    }

    fun clearChildFragmentManager() {
        for (fragment in childFragmentManager.fragments) {
            childFragmentManager.beginTransaction().remove(fragment).commit()
        }
    }

    fun clearFragmentManager() {
        for (fragment in fragmentManager!!.fragments) {
            fragmentManager!!.beginTransaction().remove(fragment).commit()
        }
    }
}
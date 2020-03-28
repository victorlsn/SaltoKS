package com.victorlsn.salto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.victorlsn.salto.R

class AccessFragment : BaseFragment() {
    override fun resumeFragment() {}

//    @Inject
//    lateinit var presenter: TrafficInfoPresenter

    override fun onResume() {
        super.onResume()
//        presenter.attachView(this)
        view?.clearFocus()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_access, container, false)
    }

}
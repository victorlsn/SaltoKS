package com.victorlsn.salto.contracts

class BaseContract {
    interface View :
        BaseView<Presenter> {
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter
}
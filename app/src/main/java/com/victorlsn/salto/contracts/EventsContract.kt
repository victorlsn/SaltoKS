package com.victorlsn.salto.contracts

import com.victorlsn.salto.data.models.LogEvent

class EventsContract {

    interface View :
        BaseView<Presenter> {
        fun onEventLogRetrieved(events: ArrayList<LogEvent>)

        fun showLoading()

        fun hideLoading()

        fun onDefaultError(error: String)
    }

    interface Presenter {
        fun getEventLog()
    }
}
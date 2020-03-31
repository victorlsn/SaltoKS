package com.victorlsn.salto.presenters

import com.victorlsn.salto.contracts.EventsContract
import com.victorlsn.salto.data.Repository
import com.victorlsn.salto.data.models.LogEvent
import com.victorlsn.salto.util.BaseSchedulerProvider
import timber.log.Timber
import javax.inject.Inject

class EventsPresenter @Inject constructor(
    private val scheduler: BaseSchedulerProvider,
    private val repository: Repository
) : BasePresenter<EventsContract.View>(),
    EventsContract.Presenter {

    override fun getEventLog() {
        disposable.add(
            repository.getEventLog()
                .observeOn(scheduler.ui())
                .subscribeOn(scheduler.io())
                .subscribe(this::eventLogRetrieved, this::defaultError)
        )
    }

    private fun eventLogRetrieved(events: ArrayList<LogEvent>) {
        if (events.isEmpty()) {
            defaultError(Error("There are no events recorded."))
        }
        view?.onEventLogRetrieved(events)
    }

    private fun defaultError(error: Throwable) {
        Timber.e(error)

        view?.onDefaultError()
    }
}
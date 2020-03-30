package com.victorlsn.salto.presenters

import android.content.Context
import com.victorlsn.salto.R
import com.victorlsn.salto.contracts.EventsContract
import com.victorlsn.salto.data.Repository
import com.victorlsn.salto.data.models.LogEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class EventsPresenter @Inject constructor(
    private val context: Context,
    private val repository: Repository
) : BasePresenter<EventsContract.View>(),
    EventsContract.Presenter {

    override fun getEventLog() {
        view?.showLoading()

        disposable.add(
            repository.getEventLog()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::eventLogRetrieved, this::defaultError)
        )
    }

    private fun eventLogRetrieved(events: ArrayList<LogEvent>) {
        view?.hideLoading()
        if (events.isEmpty()) {
            defaultError(Error(context.getString(R.string.empty_log)))
        }
        view?.onEventLogRetrieved(events)
    }

    private fun defaultError(error: Throwable) {
        Timber.e(error)

        view?.hideLoading()
        view?.onDefaultError(error.message!!)
    }
}
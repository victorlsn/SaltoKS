package com.victorlsn.salto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.victorlsn.salto.R
import com.victorlsn.salto.contracts.EventsContract
import com.victorlsn.salto.data.models.LogEvent
import com.victorlsn.salto.presenters.EventsPresenter
import com.victorlsn.salto.ui.adapters.SimpleLogEventAdapter
import kotlinx.android.synthetic.main.fragment_events.*
import javax.inject.Inject

class EventsFragment : BaseFragment(), EventsContract.View {
    override fun resumeFragment() {
        presenter.getEventLog()
    }

    @Inject
    lateinit var presenter: EventsPresenter

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    private fun setupRecyclerView(events: ArrayList<LogEvent>) {
        eventsRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = SimpleLogEventAdapter(events)
        eventsRecyclerView.adapter = adapter
    }

    override fun onDefaultError() {
        emptyLogTextView.visibility = View.VISIBLE
    }

    override fun onEventLogRetrieved(events: ArrayList<LogEvent>) {
        emptyLogTextView.visibility = View.GONE
        setupRecyclerView(events)
    }
}
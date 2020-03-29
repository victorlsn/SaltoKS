package com.victorlsn.salto.ui.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.victorlsn.salto.R
import com.victorlsn.salto.data.models.LogEvent
import com.victorlsn.salto.util.extensions.inflate

class SimpleLogEventAdapter(private val events: ArrayList<LogEvent>) :
    RecyclerView.Adapter<SimpleLogEventAdapter.ItemRowHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemRowHolder {
        val view = parent.inflate(R.layout.row_log_event, false)
        return ItemRowHolder(view)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: ItemRowHolder, position: Int) {
        val door = events[position]
        holder.bind(door)
    }

    class ItemRowHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var eventTv: TextView = itemView.findViewById(R.id.eventTextView)

        fun bind(event: LogEvent) {
            eventTv.text = event.getEventString()
        }
    }
}
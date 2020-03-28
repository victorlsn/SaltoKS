package com.victorlsn.salto.ui.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.victorlsn.salto.R
import com.victorlsn.salto.data.models.Door
import com.victorlsn.salto.listeners.DoorSelectedListener
import com.victorlsn.salto.util.extensions.inflate

class SimpleDoorAdapter(personnel: ArrayList<Door>, private val listener: DoorSelectedListener) :
    RecyclerView.Adapter<SimpleDoorAdapter.ItemRowHolder>() {

    var data: ArrayList<Door> = personnel
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemRowHolder {
        val view = parent.inflate(R.layout.row_door, false)
        return ItemRowHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ItemRowHolder, position: Int) {
        val door = data[position]
        holder.bind(door)
        holder.deleteDoorIv.setOnClickListener {
            listener.onDoorSelected(door)
        }
    }

    class ItemRowHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var doorNameTv: TextView = itemView.findViewById(R.id.doorNameTextView)
        var deleteDoorIv: ImageView = itemView.findViewById(R.id.deleteDoorImageView)

        fun bind(door: Door) {
            doorNameTv.text = door.name
        }
    }
}
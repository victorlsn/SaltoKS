package com.victorlsn.salto.ui.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.victorlsn.salto.R
import com.victorlsn.salto.data.models.Door
import com.victorlsn.salto.listeners.DoorSelectedListener
import com.victorlsn.salto.util.extensions.inflate

class RadioGridDoorAdapter(
    private val doors: ArrayList<Door>,
    private val listener: DoorSelectedListener
) : RecyclerView.Adapter<RadioGridDoorAdapter.ItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = parent.inflate(R.layout.item_radio_door, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val door = doors[position]
        holder.bind(door)
        holder.itemView.setOnClickListener {
            resetDoorsSelection()
            door.isSelected = true
            listener.onDoorSelected(door)
            notifyDataSetChanged()
        }
    }

    private fun resetDoorsSelection() {
        for (door in doors) {
            door.isSelected = false
        }
    }

    override fun getItemId(position: Int): Long {
        return doors[position].id.toLong()
    }


    override fun getItemCount(): Int {
        return doors.size
    }


    class ItemHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var doorName: TextView = itemView.findViewById(R.id.doorNameTextView)
        private var radioButton: RadioButton = itemView.findViewById(R.id.selectionRadioButton)

        fun bind(door: Door) {
            doorName.text = door.name
            radioButton.isChecked = door.isSelected
        }
    }
}


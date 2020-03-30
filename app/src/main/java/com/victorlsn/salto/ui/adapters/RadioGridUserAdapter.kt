package com.victorlsn.salto.ui.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.victorlsn.salto.R
import com.victorlsn.salto.data.models.User
import com.victorlsn.salto.listeners.UserSelectedListener
import com.victorlsn.salto.util.extensions.inflate

class RadioGridUserAdapter(private val users: ArrayList<User>,
                               private val listener: UserSelectedListener
) : RecyclerView.Adapter<RadioGridUserAdapter.ItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = parent.inflate(R.layout.item_radio_user, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
        holder.itemView.setOnClickListener {
            resetUsersSelection()
            user.isSelected = true
            listener.onUserSelected(user)
            notifyDataSetChanged()
        }
    }

    private fun resetUsersSelection() {
        for (user in users) {
            user.isSelected = false
        }
    }

    override fun getItemId(position: Int): Long {
        return users[position].id.toLong()
    }


    override fun getItemCount(): Int {
        return users.size
    }


    class ItemHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var userName: TextView = itemView.findViewById(R.id.userNameTextView)
        private var radioButton: RadioButton = itemView.findViewById(R.id.selectionRadioButton)

        fun bind(user: User) {
            userName.text = user.name
            radioButton.isChecked = user.isSelected
        }
    }
}


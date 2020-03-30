package com.victorlsn.salto.ui.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.victorlsn.salto.R
import com.victorlsn.salto.data.models.User
import com.victorlsn.salto.listeners.UserSelectedListener
import com.victorlsn.salto.util.extensions.inflate

class SimpleUserAdapter(personnel: ArrayList<User>, private val listener: UserSelectedListener) :
    RecyclerView.Adapter<SimpleUserAdapter.ItemRowHolder>() {

    var data: ArrayList<User> = personnel
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemRowHolder {
        val view = parent.inflate(R.layout.row_user, false)
        return ItemRowHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ItemRowHolder, position: Int) {
        val user = data[position]
        holder.bind(user)
        holder.deleteUserIv.setOnClickListener {
            listener.onUserSelected(user)
        }
    }

    class ItemRowHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var userNameTv: TextView = itemView.findViewById(R.id.userNameTextView)
        var deleteUserIv: ImageView = itemView.findViewById(R.id.deleteUserImageView)

        fun bind(user: User) {
            userNameTv.text = user.name
        }
    }
}
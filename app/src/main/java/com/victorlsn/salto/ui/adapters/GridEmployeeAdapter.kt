package com.victorlsn.salto.ui.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.victorlsn.salto.R
import com.victorlsn.salto.data.models.Door
import com.victorlsn.salto.data.models.User
import com.victorlsn.salto.listeners.UserAuthorizedListener
import com.victorlsn.salto.util.extensions.inflate

class GridUserAdapter(private val door: Door,
                      private val users: ArrayList<User>,
                      private val listener: UserAuthorizedListener
) : RecyclerView.Adapter<GridUserAdapter.ItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = parent.inflate(R.layout.item_user, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val user = users[position]
        val hasPermission = door.isUserAuthorized(user)
        holder.bind(user, hasPermission)
        holder.permissionCheckBox.setOnCheckedChangeListener { _, isChecked ->
            listener.onUserAuthorizationChanged(user, isChecked)
        }
    }



    override fun getItemCount(): Int {
        return users.size
    }


    class ItemHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var userName: TextView = itemView.findViewById(R.id.userNameTextView)
        var permissionCheckBox : CheckBox = itemView.findViewById(R.id.permissionCheckbox)

        fun bind(user: User, hasPermission: Boolean) {
            userName.text = user.name
            permissionCheckBox.isChecked = hasPermission
        }
    }
}


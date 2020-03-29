package com.victorlsn.salto.ui.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.victorlsn.salto.R
import com.victorlsn.salto.data.models.Door
import com.victorlsn.salto.data.models.Employee
import com.victorlsn.salto.listeners.EmployeeAuthorizedListener
import com.victorlsn.salto.util.extensions.inflate

class GridEmployeeAdapter(private val door: Door,
                          private val employees: ArrayList<Employee>,
                          private val listener: EmployeeAuthorizedListener
) : RecyclerView.Adapter<GridEmployeeAdapter.ItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = parent.inflate(R.layout.item_employee, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val employee = employees[position]
        val hasPermission = door.isUserAuthorized(employee)
        holder.bind(employee, hasPermission)
        holder.permissionCheckBox.setOnCheckedChangeListener { _, isChecked ->
            listener.onEmployeeAuthorizationChanged(employee, isChecked)
        }
    }



    override fun getItemCount(): Int {
        return employees.size
    }


    class ItemHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var employeeName: TextView = itemView.findViewById(R.id.employeeNameTextView)
        var permissionCheckBox : CheckBox = itemView.findViewById(R.id.permissionCheckbox)

        fun bind(employee: Employee, hasPermission: Boolean) {
            employeeName.text = employee.name
            permissionCheckBox.isChecked = hasPermission
        }
    }
}


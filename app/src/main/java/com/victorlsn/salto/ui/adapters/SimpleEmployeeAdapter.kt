package com.victorlsn.salto.ui.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.victorlsn.salto.R
import com.victorlsn.salto.data.models.Employee
import com.victorlsn.salto.listeners.EmployeeSelectedListener
import com.victorlsn.salto.util.extensions.inflate

class SimpleEmployeeAdapter(personnel: ArrayList<Employee>, private val listener: EmployeeSelectedListener) :
    RecyclerView.Adapter<SimpleEmployeeAdapter.ItemRowHolder>() {

    var data: ArrayList<Employee> = personnel
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemRowHolder {
        val view = parent.inflate(R.layout.row_employee, false)
        return ItemRowHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ItemRowHolder, position: Int) {
        val employee = data[position]
        holder.bind(employee)
        holder.deleteEmployeeIv.setOnClickListener {
            listener.onEmployeeSelected(employee)
        }
    }

    class ItemRowHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var employeeNameTv: TextView = itemView.findViewById(R.id.employeeNameTextView)
        var deleteEmployeeIv: ImageView = itemView.findViewById(R.id.deleteEmployeeImageView)

        fun bind(employee: Employee) {
            employeeNameTv.text = employee.name
        }
    }
}
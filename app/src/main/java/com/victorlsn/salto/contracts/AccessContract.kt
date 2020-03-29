package com.victorlsn.salto.contracts

import com.victorlsn.salto.data.models.Door
import com.victorlsn.salto.data.models.Employee

class AccessContract {

    interface View :
        BaseView<Presenter> {
        fun onGetEmployeesSuccess(employees: ArrayList<Employee>)

        fun onGetDoorsSuccess(doors: ArrayList<Door>)

        fun onDefaultError(error: String)

        fun showLoading()

        fun hideLoading()
    }

    interface Presenter {
        fun getDoors()

        fun getEmployees()

        fun changePermission(employee: Employee, door: Door, authorized: Boolean)
    }
}
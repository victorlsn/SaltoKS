package com.victorlsn.salto.contracts

import com.victorlsn.salto.data.models.Employee

class EmployeesContract {

    interface View :
        BaseView<Presenter> {
        fun onAddNewEmployeeSuccess()

        fun onAddNewEmployeeFailure(error: String)

        fun onRemoveEmployeeSuccess()

        fun onGetEmployeesSuccess(employees: ArrayList<Employee>)

        fun onDefaultError(error: String)

        fun showLoading()

        fun hideLoading()
    }

    interface Presenter {
        fun addNewEmployee(name: String)

        fun removeEmployee(employee: Employee)

        fun getEmployees()
    }
}
package com.victorlsn.salto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.victorlsn.salto.R
import com.victorlsn.salto.contracts.EmployeesContract
import com.victorlsn.salto.data.models.Employee
import com.victorlsn.salto.listeners.EmployeeSelectedListener
import com.victorlsn.salto.presenters.EmployeesPresenter
import com.victorlsn.salto.ui.adapters.SimpleEmployeeAdapter
import kotlinx.android.synthetic.main.fragment_employees.*
import javax.inject.Inject

class EmployeesFragment : BaseFragment(), EmployeesContract.View {
    override fun resumeFragment() {}

    @Inject
    lateinit var presenter: EmployeesPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
        presenter.getEmployees()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_employees, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAddEmployeeButton()
    }

    private fun setupAddEmployeeButton() {
        addEmployeesButton.setOnClickListener {
            presenter.addNewEmployee(nameInputLayout.editText?.text.toString())
        }
    }


    private fun setupRecyclerView(employees: ArrayList<Employee>) {
        employeesRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = SimpleEmployeeAdapter(employees, object : EmployeeSelectedListener {
            override fun onEmployeeSelected(employee: Employee) {
                presenter.removeEmployee(employee)
            }

        })
        employeesRecyclerView.adapter = adapter
    }

    override fun onAddNewEmployeeSuccess() {
        nameInputLayout.editText?.setText("")
        nameInputLayout.error = null

        presenter.getEmployees()
        Toast.makeText(context!!, "Employee added successfully", Toast.LENGTH_LONG).show()
    }

    override fun onAddNewEmployeeFailure(error: String) {
        nameInputLayout.error = error
    }

    override fun onRemoveEmployeeSuccess() {
        presenter.getEmployees()
        Toast.makeText(context!!, "Employee removed successfully", Toast.LENGTH_LONG).show()
    }

    override fun onGetEmployeesSuccess(employees: ArrayList<Employee>) {
        setupRecyclerView(employees)
    }

    override fun onDefaultError(error: String) {
        Toast.makeText(context!!, error, Toast.LENGTH_LONG).show()
    }

    override fun showLoading() {
        if (!loading.isShowing) {
            loading.show()
        }
    }

    override fun hideLoading() {
        if (loading.isShowing) {
            loading.dismiss()
        }
    }
}
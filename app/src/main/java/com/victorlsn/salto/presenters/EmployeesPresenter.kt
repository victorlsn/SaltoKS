package com.victorlsn.salto.presenters

import com.victorlsn.salto.contracts.EmployeesContract
import com.victorlsn.salto.data.Repository
import com.victorlsn.salto.data.models.Employee
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class EmployeesPresenter @Inject constructor(
    private val repository: Repository
) : BasePresenter<EmployeesContract.View>(),
    EmployeesContract.Presenter {

    override fun getEmployees() {
        view?.showLoading()

        disposable.add(
            repository.getEmployees()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::employeesRetrieved, this::defaultError)
        )
    }

    private fun employeesRetrieved(employees: ArrayList<Employee>) {
        view?.hideLoading()
        view?.onGetEmployeesSuccess(employees)
    }

    override fun addNewEmployee(name: String) {
        view?.showLoading()
        if (name.isEmpty()) {
            newEmployeeAddFailure(Error("You can't add an empty-named employee."))
            return
        }

        val employee = Employee(name)

        disposable.add(
            repository.addEmployee(employee)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::newEmployeeAddedSuccessfully, this::newEmployeeAddFailure)
        )
    }

    private fun newEmployeeAddedSuccessfully(success: Boolean) {
        Timber.d("Add Employee call successful")
        view?.hideLoading()

        if (success) {
            view?.onAddNewEmployeeSuccess()
        }
        else {
            newEmployeeAddFailure(Error("An employee with that name already exists."))
        }
    }

    private fun newEmployeeAddFailure(error: Throwable) {
        Timber.e(error)
        view?.hideLoading()

        view?.onAddNewEmployeeFailure(error.message!!)
    }

    override fun removeEmployee(employee: Employee) {
        view?.showLoading()

        disposable.add(
            repository.removeEmployee(employee)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::employeeRemovedSuccessfully, this::defaultError)
        )
    }

    private fun employeeRemovedSuccessfully(success: Boolean) {
        Timber.d("Remove employee call successful")
        view?.hideLoading()

        if (success) {
            view?.onRemoveEmployeeSuccess()
        }
        else {
            defaultError(Error("This employee doesn't exist anymore."))
        }
    }

    private fun defaultError(error: Throwable) {
        Timber.e(error)

        view?.hideLoading()
        view?.onDefaultError(error.message!!)
    }
}
package com.victorlsn.salto.presenters

import android.content.Context
import com.victorlsn.salto.R
import com.victorlsn.salto.contracts.AccessContract
import com.victorlsn.salto.data.Repository
import com.victorlsn.salto.data.models.Door
import com.victorlsn.salto.data.models.Employee
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class AccessPresenter @Inject constructor(
    private val context: Context,
    private val repository: Repository
) : BasePresenter<AccessContract.View>(),
    AccessContract.Presenter {

    override fun getDoors() {
        view?.showLoading()

        disposable.add(
            repository.getDoors()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::doorsRetrieved, this::defaultError)
        )
    }

    private fun doorsRetrieved(doors: ArrayList<Door>) {
        view?.hideLoading()
        view?.onGetDoorsSuccess(doors)
    }

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

    override fun changePermission(employee: Employee, door: Door, authorized: Boolean) {
        view?.showLoading()

        disposable.add(
            repository.changeEmployeePermissionForDoor(employee, door, authorized)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::authorizationChanged, this::defaultError)
        )
    }

    private fun authorizationChanged(success: Boolean) {
        Timber.d("Change Authorization call successful")
        view?.hideLoading()

        if (success) {
            view?.onPermissionsChanged()
        }
        else {
//            newDoorAddFailure(Error(context.getString(R.string.door_exists)))
        }
    }

    private fun defaultError(error: Throwable) {
        Timber.e(error)

        view?.hideLoading()
        view?.onDefaultError(error.message!!)
    }
}
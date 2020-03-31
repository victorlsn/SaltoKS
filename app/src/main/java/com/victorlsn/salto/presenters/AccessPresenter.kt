package com.victorlsn.salto.presenters

import com.victorlsn.salto.contracts.AccessContract
import com.victorlsn.salto.data.Repository
import com.victorlsn.salto.data.models.Door
import com.victorlsn.salto.data.models.User
import com.victorlsn.salto.util.BaseSchedulerProvider
import timber.log.Timber
import javax.inject.Inject

class AccessPresenter @Inject constructor(
    private val scheduler: BaseSchedulerProvider,
    private val repository: Repository
) : BasePresenter<AccessContract.View>(),
    AccessContract.Presenter {

    override fun getDoors() {
        disposable.add(
            repository.getDoors()
                .observeOn(scheduler.ui())
                .subscribeOn(scheduler.io())
                .subscribe(this::doorsRetrieved, this::defaultError)
        )
    }

    private fun doorsRetrieved(doors: ArrayList<Door>) {
        view?.onGetDoorsSuccess(doors)
    }

    override fun getUsers() {
        disposable.add(
            repository.getUsers()
                .observeOn(scheduler.ui())
                .subscribeOn(scheduler.io())
                .subscribe(this::usersRetrieved, this::defaultError)
        )
    }

    private fun usersRetrieved(users: ArrayList<User>) {
        view?.onGetUsersSuccess(users)
    }

    override fun changePermission(user: User, door: Door, authorized: Boolean) {
        disposable.add(
            repository.changeUserPermissionForDoor(user, door, authorized)
                .observeOn(scheduler.ui())
                .subscribeOn(scheduler.io())
                .subscribe(this::authorizationChanged, this::defaultError)
        )
    }

    private fun authorizationChanged(success: Boolean) {
        if (!success) {
            defaultError(Error("It was not possible to change permissions for this door/user."))
        }
    }

    private fun defaultError(error: Throwable) {
        Timber.e(error)
        view?.onDefaultError(error.message!!)
    }
}
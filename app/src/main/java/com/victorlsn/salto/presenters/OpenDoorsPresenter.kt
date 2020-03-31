package com.victorlsn.salto.presenters

import com.victorlsn.salto.contracts.OpenDoorsContract
import com.victorlsn.salto.data.Repository
import com.victorlsn.salto.data.models.Door
import com.victorlsn.salto.data.models.User
import com.victorlsn.salto.util.BaseSchedulerProvider
import timber.log.Timber
import javax.inject.Inject

class OpenDoorsPresenter @Inject constructor(
    private val scheduler: BaseSchedulerProvider,
    private val repository: Repository
) : BasePresenter<OpenDoorsContract.View>(),
    OpenDoorsContract.Presenter {

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

    override fun openDoor(door: Door?, user: User?) {
        if (door == null || user == null) {
            defaultError(Error("You must select a user and a door."))
            return
        }

        disposable.add(
            repository.openDoor(door, user)
                .observeOn(scheduler.ui())
                .subscribeOn(scheduler.io())
                .subscribe(this::triedToOpenDoor, this::defaultError)
        )
    }

    private fun triedToOpenDoor(success: Boolean) {
        view?.onOpenDoor(success)
    }

    private fun defaultError(error: Throwable) {
        Timber.e(error)
        view?.onDefaultError(error.message!!)
    }
}
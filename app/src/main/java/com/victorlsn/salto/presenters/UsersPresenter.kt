package com.victorlsn.salto.presenters

import com.victorlsn.salto.contracts.UsersContract
import com.victorlsn.salto.data.Repository
import com.victorlsn.salto.data.models.User
import com.victorlsn.salto.util.BaseSchedulerProvider
import timber.log.Timber
import javax.inject.Inject

class UsersPresenter @Inject constructor(
    private val scheduler: BaseSchedulerProvider,
    private val repository: Repository
) : BasePresenter<UsersContract.View>(),
    UsersContract.Presenter {

    override fun getUsers() {
        view?.showLoading()

        disposable.add(
            repository.getUsers()
                .observeOn(scheduler.ui())
                .subscribeOn(scheduler.io())
                .subscribe(this::usersRetrieved, this::defaultError)
        )
    }

    private fun usersRetrieved(users: ArrayList<User>) {
        view?.hideLoading()
        view?.onGetUsersSuccess(users)
    }

    override fun addNewUser(name: String) {
        view?.showLoading()
        if (name.isEmpty()) {
            newUserAddFailure(Error("You can't add an empty-named user."))
            return
        }

        val user = User(name.trim())

        disposable.add(
            repository.addUser(user)
                .observeOn(scheduler.ui())
                .subscribeOn(scheduler.io())
                .subscribe(this::newUserAddedSuccessfully, this::newUserAddFailure)
        )
    }

    private fun newUserAddedSuccessfully(success: Boolean) {
        if (success) {
            view?.hideLoading()
            view?.onAddNewUserSuccess()
        }
        else {
            newUserAddFailure(Error("A user with that name already exists."))
        }
    }

    private fun newUserAddFailure(error: Throwable) {
        Timber.e(error)
        view?.hideLoading()

        view?.onAddNewUserFailure(error.message!!)
    }

    override fun removeUser(user: User) {
        view?.showLoading()

        disposable.add(
            repository.removeUser(user)
                .observeOn(scheduler.ui())
                .subscribeOn(scheduler.io())
                .subscribe(this::userRemovedSuccessfully, this::defaultError)
        )
    }

    private fun userRemovedSuccessfully(success: Boolean) {
        if (success) {
            view?.hideLoading()
            view?.onRemoveUserSuccess()
        }
        else {
            defaultError(Error("This user doesn't exist anymore."))
        }
    }

    private fun defaultError(error: Throwable) {
        Timber.e(error)

        view?.hideLoading()
        view?.onDefaultError(error.message!!)
    }
}
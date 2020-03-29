package com.victorlsn.salto.presenters

import com.victorlsn.salto.contracts.UsersContract
import com.victorlsn.salto.data.Repository
import com.victorlsn.salto.data.models.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class UsersPresenter @Inject constructor(
    private val repository: Repository
) : BasePresenter<UsersContract.View>(),
    UsersContract.Presenter {

    override fun getUsers() {
        view?.showLoading()

        disposable.add(
            repository.getUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::newUserAddedSuccessfully, this::newUserAddFailure)
        )
    }

    private fun newUserAddedSuccessfully(success: Boolean) {
        Timber.d("Add User call successful")
        view?.hideLoading()

        if (success) {
            view?.onAddNewUserSuccess()
        }
        else {
            newUserAddFailure(Error("An user with that name already exists."))
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::userRemovedSuccessfully, this::defaultError)
        )
    }

    private fun userRemovedSuccessfully(success: Boolean) {
        Timber.d("Remove user call successful")
        view?.hideLoading()

        if (success) {
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
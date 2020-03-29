package com.victorlsn.salto.presenters

import android.content.Context
import com.victorlsn.salto.R
import com.victorlsn.salto.contracts.AccessContract
import com.victorlsn.salto.data.Repository
import com.victorlsn.salto.data.models.Door
import com.victorlsn.salto.data.models.User
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
        disposable.add(
            repository.getDoors()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::doorsRetrieved, this::defaultError)
        )
    }

    private fun doorsRetrieved(doors: ArrayList<Door>) {
        view?.onGetDoorsSuccess(doors)
    }

    override fun getUsers() {
        disposable.add(
            repository.getUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::usersRetrieved, this::defaultError)
        )
    }

    private fun usersRetrieved(users: ArrayList<User>) {
        view?.onGetUsersSuccess(users)
    }

    override fun changePermission(user: User, door: Door, authorized: Boolean) {
        disposable.add(
            repository.changeUserPermissionForDoor(user, door, authorized)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::authorizationChanged, this::defaultError)
        )
    }

    private fun authorizationChanged(success: Boolean) {
        Timber.d("Change Authorization call successful")
        if (!success) {
            defaultError(Error(context.getString(R.string.permission_change_error)))
        }
    }

    private fun defaultError(error: Throwable) {
        Timber.e(error)
        view?.onDefaultError(error.message!!)
    }
}
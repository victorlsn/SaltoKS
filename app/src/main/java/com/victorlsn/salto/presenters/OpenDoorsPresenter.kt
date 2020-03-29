package com.victorlsn.salto.presenters

import android.content.Context
import com.victorlsn.salto.R
import com.victorlsn.salto.contracts.OpenDoorsContract
import com.victorlsn.salto.data.Repository
import com.victorlsn.salto.data.models.Door
import com.victorlsn.salto.data.models.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class OpenDoorsPresenter @Inject constructor(
    private val context: Context,
    private val repository: Repository
) : BasePresenter<OpenDoorsContract.View>(),
    OpenDoorsContract.Presenter {

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

    override fun openDoor(door: Door?, user: User?) {
        if (door == null || user == null) {
            defaultError(Error(context.getString(R.string.must_select_user_and_door)))
            return
        }

        disposable.add(
            repository.openDoor(user, door)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::triedToOpenDoor, this::defaultError)
        )
    }

    private fun triedToOpenDoor(success: Boolean) {
        Timber.d("Tried to open door")
        view?.onOpenDoor(success)
    }

    private fun defaultError(error: Throwable) {
        Timber.e(error)
        view?.onDefaultError(error.message!!)
    }
}
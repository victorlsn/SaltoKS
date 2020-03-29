package com.victorlsn.salto.presenters

import android.content.Context
import com.victorlsn.salto.R
import com.victorlsn.salto.contracts.DoorsContract
import com.victorlsn.salto.data.Repository
import com.victorlsn.salto.data.models.Door
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class DoorsPresenter @Inject constructor(
    private val context: Context,
    private val repository: Repository
) : BasePresenter<DoorsContract.View>(),
    DoorsContract.Presenter {

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

    override fun addNewDoor(name: String) {
        view?.showLoading()
        if (name.isEmpty()) {
            newDoorAddFailure(Error("You can't add an empty-named door."))
            return
        }

        val door = Door(name)

        disposable.add(
            repository.addDoor(door)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::newDoorAddedSuccessfully, this::newDoorAddFailure)
        )
    }

    private fun newDoorAddedSuccessfully(success: Boolean) {
        Timber.d("Add Door call successful")
        view?.hideLoading()

        if (success) {
            view?.onAddNewDoorSuccess()
        }
        else {
            newDoorAddFailure(Error(context.getString(R.string.door_exists_error)))
        }
    }

    private fun newDoorAddFailure(error: Throwable) {
        Timber.e(error)
        view?.hideLoading()

        view?.onAddNewDoorFailure(error.message!!)
    }

    override fun removeDoor(door: Door) {
        view?.showLoading()

        disposable.add(
            repository.removeDoor(door)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::doorRemovedSuccessfully, this::defaultError)
        )
    }

    private fun doorRemovedSuccessfully(success: Boolean) {
        Timber.d("Remove door call successful")
        view?.hideLoading()

        if (success) {
            view?.onRemoveDoorSuccess()
        }
        else {
            defaultError(Error(context.getString(R.string.door_does_not_exist_error)))
        }
    }

    private fun defaultError(error: Throwable) {
        Timber.e(error)

        view?.hideLoading()
        view?.onDefaultError(error.message!!)
    }
}
package com.victorlsn.salto.presenters

import com.victorlsn.salto.contracts.DoorsContract
import com.victorlsn.salto.data.Repository
import com.victorlsn.salto.data.models.Door
import com.victorlsn.salto.util.BaseSchedulerProvider
import timber.log.Timber
import javax.inject.Inject

class DoorsPresenter @Inject constructor(
    private val scheduler: BaseSchedulerProvider,
    private val repository: Repository
) : BasePresenter<DoorsContract.View>(),
    DoorsContract.Presenter {

    override fun getDoors() {
        view?.showLoading()

        disposable.add(
            repository.getDoors()
                .observeOn(scheduler.ui())
                .subscribeOn(scheduler.io())
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

        val door = Door(name.trim())

        disposable.add(
            repository.addDoor(door)
                .observeOn(scheduler.ui())
                .subscribeOn(scheduler.io())
                .subscribe(this::newDoorAddedSuccessfully, this::newDoorAddFailure)
        )
    }

    private fun newDoorAddedSuccessfully(success: Boolean) {
        if (success) {
            view?.hideLoading()
            view?.onAddNewDoorSuccess()
        } else {
            newDoorAddFailure(Error("A door with that name already exists."))
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
                .observeOn(scheduler.ui())
                .subscribeOn(scheduler.io())
                .subscribe(this::doorRemovedSuccessfully, this::defaultError)
        )
    }

    private fun doorRemovedSuccessfully(success: Boolean) {
        if (success) {
            view?.hideLoading()
            view?.onRemoveDoorSuccess()
        } else {
            defaultError(Error("This door doesn't exist anymore."))
        }
    }

    private fun defaultError(error: Throwable) {
        Timber.e(error)

        view?.hideLoading()
        view?.onDefaultError(error.message!!)
    }
}
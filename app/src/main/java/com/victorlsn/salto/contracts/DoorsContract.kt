package com.victorlsn.salto.contracts

import com.victorlsn.salto.data.models.Door

class DoorsContract {

    interface View :
        BaseView<Presenter> {
        fun onAddNewDoorSuccess()

        fun onAddNewDoorFailure(error: String)

        fun onRemoveDoorSuccess()

        fun onGetDoorsSuccess(doors: ArrayList<Door>)

        fun onDefaultError(error: String)

        fun showLoading()

        fun hideLoading()
    }

    interface Presenter {
        fun addNewDoor(name: String)

        fun removeDoor(door: Door)

        fun getDoors()
    }
}
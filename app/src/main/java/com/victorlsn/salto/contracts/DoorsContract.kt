package com.victorlsn.salto.contracts

import com.victorlsn.salto.data.models.Door
import com.victorlsn.salto.data.models.Employee

class DoorsContract {

    interface View :
        BaseView<Presenter> {
        fun onAddNewDoorSuccess()

        fun onAddNewDoorFailure(error: String)

        fun onRemoveDoorSuccess()

        fun onGetDoorsSuccess(door: ArrayList<Door>)

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
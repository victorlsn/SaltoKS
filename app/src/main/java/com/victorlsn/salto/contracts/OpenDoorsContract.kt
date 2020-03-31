package com.victorlsn.salto.contracts

import com.victorlsn.salto.data.models.Door
import com.victorlsn.salto.data.models.User

class OpenDoorsContract {

    interface View :
        BaseView<Presenter> {
        fun onGetUsersSuccess(users: ArrayList<User>)

        fun onGetDoorsSuccess(doors: ArrayList<Door>)

        fun onOpenDoor(success: Boolean)

        fun onDefaultError(error: String)
    }

    interface Presenter {
        fun openDoor(door: Door?, user: User?)

        fun getUsers()

        fun getDoors()
    }
}
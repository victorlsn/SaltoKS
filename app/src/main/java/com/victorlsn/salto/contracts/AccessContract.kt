package com.victorlsn.salto.contracts

import com.victorlsn.salto.data.models.Door
import com.victorlsn.salto.data.models.User

class AccessContract {

    interface View :
        BaseView<Presenter> {
        fun onGetUsersSuccess(users: ArrayList<User>)

        fun onGetDoorsSuccess(doors: ArrayList<Door>)

        fun onDefaultError(error: String)
    }

    interface Presenter {
        fun getDoors()

        fun getUsers()

        fun changePermission(user: User, door: Door, authorized: Boolean)
    }
}
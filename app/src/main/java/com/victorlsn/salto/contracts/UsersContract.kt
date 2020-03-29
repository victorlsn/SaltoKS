package com.victorlsn.salto.contracts

import com.victorlsn.salto.data.models.User

class UsersContract {

    interface View :
        BaseView<Presenter> {
        fun onAddNewUserSuccess()

        fun onAddNewUserFailure(error: String)

        fun onRemoveUserSuccess()

        fun onGetUsersSuccess(users: ArrayList<User>)

        fun onDefaultError(error: String)

        fun showLoading()

        fun hideLoading()
    }

    interface Presenter {
        fun addNewUser(name: String)

        fun removeUser(user: User)

        fun getUsers()
    }
}
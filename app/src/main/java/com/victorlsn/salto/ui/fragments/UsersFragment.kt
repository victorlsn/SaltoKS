package com.victorlsn.salto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.victorlsn.salto.R
import com.victorlsn.salto.contracts.UsersContract
import com.victorlsn.salto.data.models.User
import com.victorlsn.salto.listeners.UserSelectedListener
import com.victorlsn.salto.presenters.UsersPresenter
import com.victorlsn.salto.ui.adapters.SimpleUserAdapter
import com.victorlsn.salto.util.ToastHelper
import kotlinx.android.synthetic.main.fragment_users.*
import javax.inject.Inject

class UsersFragment : BaseFragment(), UsersContract.View {
    override fun resumeFragment() {}

    @Inject
    lateinit var presenter: UsersPresenter

    @Inject
    lateinit var toastHelper : ToastHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
        presenter.getUsers()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAddUserButton()
    }

    private fun setupAddUserButton() {
        addUsersButton.setOnClickListener {
            presenter.addNewUser(nameInputLayout.editText?.text.toString())
        }
    }


    private fun setupRecyclerView(users: ArrayList<User>) {
        usersRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = SimpleUserAdapter(users, object : UserSelectedListener {
            override fun onUserSelected(user: User) {
                presenter.removeUser(user)
            }

        })
        usersRecyclerView.adapter = adapter
    }

    override fun onAddNewUserSuccess() {
        nameInputLayout.editText?.setText("")
        nameInputLayout.error = null

        presenter.getUsers()
        toastHelper.showToast(context, "User added successfully")
    }

    override fun onAddNewUserFailure(error: String) {
        nameInputLayout.error = error
    }

    override fun onRemoveUserSuccess() {
        presenter.getUsers()
        toastHelper.showToast(context, "User removed successfully")
    }

    override fun onGetUsersSuccess(users: ArrayList<User>) {
        setupRecyclerView(users)
    }

    override fun onDefaultError(error: String) {
        toastHelper.showToast(context, error)
    }

    override fun showLoading() {
        if (!loading.isShowing) {
            loading.show()
        }
    }

    override fun hideLoading() {
        if (loading.isShowing) {
            loading.dismiss()
        }
    }
}
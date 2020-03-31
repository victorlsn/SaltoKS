package com.victorlsn.salto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.GridLayoutManager
import com.victorlsn.salto.R
import com.victorlsn.salto.contracts.AccessContract
import com.victorlsn.salto.data.models.Door
import com.victorlsn.salto.data.models.User
import com.victorlsn.salto.listeners.DoorSelectedListener
import com.victorlsn.salto.listeners.UserAuthorizedListener
import com.victorlsn.salto.presenters.AccessPresenter
import com.victorlsn.salto.ui.adapters.GridDoorAdapter
import com.victorlsn.salto.ui.adapters.GridUserAdapter
import com.victorlsn.salto.util.ToastHelper
import kotlinx.android.synthetic.main.fragment_access.*
import javax.inject.Inject

class AccessFragment : BaseFragment(), AccessContract.View {
    override fun resumeFragment() {
        presenter.getDoors()
        presenter.getUsers()
    }

    @Inject
    lateinit var presenter: AccessPresenter

    @Inject
    lateinit var toastHelper: ToastHelper

    private var doors: ArrayList<Door>? = null
    private var currentlySelectedDoor: Door? = null
    private var users: ArrayList<User>? = null

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_access, container, false)
    }

    private fun setupDoors(doors: ArrayList<Door>) {
        this.doors = doors
        if (doors.isEmpty()) {
            doorsTextView.text = getString(R.string.no_doors_text)
        } else {
            doorsTextView.text = getString(R.string.doors_access_text)
        }
        setupDoorsRecyclerView()
    }

    private fun setupDoorsRecyclerView() {
        val gridDoorAdapter =
            GridDoorAdapter(doors!!,
                object : DoorSelectedListener {
                    override fun onDoorSelected(door: Door) {
                        currentlySelectedDoor = door
                        usersTextView.visibility = View.VISIBLE
                        setupUsersRecyclerView()
                    }
                }
            )
        gridDoorAdapter.setHasStableIds(true)

        val layoutManager = GridLayoutManager(context, 3)

        doorsRecyclerView.layoutManager = layoutManager
        doorsRecyclerView.adapter = gridDoorAdapter

        doorsRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                doorsRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                autoSelectPreviouslySelectedDoor()
            }

            private fun autoSelectPreviouslySelectedDoor() {
                currentlySelectedDoor?.let {
                    val id = it.id
                    val holder = doorsRecyclerView.findViewHolderForItemId(id.toLong())
                    val itemView = holder?.itemView

                    itemView?.performClick()
                }
            }
        })
    }

    override fun onGetUsersSuccess(users: ArrayList<User>) {
        if (this.users != users) {
            setupUsers(users)
        }
    }

    private fun setupUsers(users: ArrayList<User>) {
        this.users = users
        if (users.isEmpty()) {
            usersTextView.text = getString(R.string.no_users_text)
        } else {
            usersTextView.text = getString(R.string.users_access_text)
        }
        if (currentlySelectedDoor != null) {
            setupUsersRecyclerView()
        }
    }

    private fun setupUsersRecyclerView() {
        val gridUserAdapter =
            GridUserAdapter(currentlySelectedDoor!!, users!!,
                object : UserAuthorizedListener {
                    override fun onUserAuthorizationChanged(
                        user: User,
                        authorized: Boolean
                    ) {
                        presenter.changePermission(user, currentlySelectedDoor!!, authorized)
                    }
                }
            )
        gridUserAdapter.setHasStableIds(true)

        val layoutManager = GridLayoutManager(context, 4)

        usersRecyclerView.layoutManager = layoutManager
        usersRecyclerView.adapter = gridUserAdapter
    }

    override fun onGetDoorsSuccess(doors: ArrayList<Door>) {
        if (this.doors != doors) {
            setupDoors(doors)
        }
    }

    override fun onDefaultError(error: String) {
        toastHelper.showToast(context, error)
    }
}
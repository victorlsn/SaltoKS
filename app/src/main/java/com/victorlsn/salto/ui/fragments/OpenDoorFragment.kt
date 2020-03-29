package com.victorlsn.salto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.victorlsn.salto.R
import com.victorlsn.salto.contracts.OpenDoorsContract
import com.victorlsn.salto.data.models.Door
import com.victorlsn.salto.data.models.User
import com.victorlsn.salto.listeners.DoorSelectedListener
import com.victorlsn.salto.listeners.UserSelectedListener
import com.victorlsn.salto.presenters.AccessPresenter
import com.victorlsn.salto.presenters.OpenDoorsPresenter
import com.victorlsn.salto.ui.adapters.RadioGridDoorAdapter
import com.victorlsn.salto.ui.adapters.RadioGridUserAdapter
import kotlinx.android.synthetic.main.fragment_access.*
import kotlinx.android.synthetic.main.fragment_access.doorsRecyclerView
import kotlinx.android.synthetic.main.fragment_access.doorsTextView
import kotlinx.android.synthetic.main.fragment_access.usersRecyclerView
import kotlinx.android.synthetic.main.fragment_access.usersTextView
import kotlinx.android.synthetic.main.fragment_open_door.*
import javax.inject.Inject

class OpenDoorFragment : BaseFragment(), OpenDoorsContract.View {
    override fun resumeFragment() {
        presenter.getDoors()
        presenter.getUsers()
    }

    @Inject
    lateinit var presenter: OpenDoorsPresenter

    private var doors: ArrayList<Door>? = null
    private var currentlySelectedDoor: Door? = null
    private var users: ArrayList<User>? = null
    private var currentlySelectedUser: User? = null

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_open_door, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        openDoorButton.setOnClickListener {
            presenter.openDoor(currentlySelectedDoor, currentlySelectedUser)
        }
    }

    private fun setupDoors(doors: ArrayList<Door>) {
        this.doors = doors
        if (doors.isEmpty()) {
            doorsTextView.text = getString(R.string.no_doors_text)
        } else {
            doorsTextView.text = getString(R.string.select_door)
        }
        setupDoorsRecyclerView()
    }

    private fun setupDoorsRecyclerView() {
        val radioGridDoorAdapter =
            RadioGridDoorAdapter(doors!!,
                object : DoorSelectedListener {
                    override fun onDoorSelected(door: Door) {
                        currentlySelectedDoor = door
                    }
                }
            )
        radioGridDoorAdapter.setHasStableIds(true)

        val layoutManager = GridLayoutManager(context, 3)

        doorsRecyclerView.layoutManager = layoutManager
        doorsRecyclerView.adapter = radioGridDoorAdapter

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

    private fun setupUsers(users: ArrayList<User>) {
        this.users = users
        if (users.isEmpty()) {
            usersTextView.text = getString(R.string.no_users_text)
        } else {
            usersTextView.text = getString(R.string.select_user)
        }
        setupUsersRecyclerView()
    }

    private fun setupUsersRecyclerView() {
        val radioGridUserAdapter =
            RadioGridUserAdapter(users!!,
                object : UserSelectedListener {
                    override fun onUserSelected(user: User) {
                        currentlySelectedUser = user
                    }
                }
            )
        radioGridUserAdapter.setHasStableIds(true)

        val layoutManager = GridLayoutManager(context, 4)

        usersRecyclerView.layoutManager = layoutManager
        usersRecyclerView.adapter = radioGridUserAdapter
    }

    override fun onGetDoorsSuccess(doors: ArrayList<Door>) {
        if (this.doors != doors) {
            setupDoors(doors)
        }
    }

    override fun onGetUsersSuccess(users: ArrayList<User>) {
        if (this.users != users) {
            setupUsers(users)
        }
    }

    override fun onOpenDoor(success: Boolean) {
        // TODO Add animation for door opening/not opening
        if (success) {
            Toast.makeText(context, "${currentlySelectedUser!!.name} opened ${currentlySelectedDoor!!.name}.", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(context, "${currentlySelectedUser!!.name} tried to open ${currentlySelectedDoor!!.name}, but has no permission.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDefaultError(error: String) {
        Toast.makeText(context!!, error, Toast.LENGTH_SHORT).show()
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
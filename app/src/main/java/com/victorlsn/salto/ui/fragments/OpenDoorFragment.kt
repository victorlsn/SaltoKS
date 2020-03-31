package com.victorlsn.salto.ui.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.TransitionManager
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.victorlsn.salto.R
import com.victorlsn.salto.contracts.OpenDoorsContract
import com.victorlsn.salto.data.models.Door
import com.victorlsn.salto.data.models.User
import com.victorlsn.salto.listeners.DoorSelectedListener
import com.victorlsn.salto.listeners.UserSelectedListener
import com.victorlsn.salto.presenters.OpenDoorsPresenter
import com.victorlsn.salto.ui.adapters.RadioGridDoorAdapter
import com.victorlsn.salto.ui.adapters.RadioGridUserAdapter
import com.victorlsn.salto.util.ToastHelper
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

    @Inject
    lateinit var toastHelper: ToastHelper

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
        if (success) {
            morphButton(
                R.string.green_color,
                R.drawable.ic_keyhole_to_success,
                R.drawable.ic_success_to_keyhole
            )
            showDoorOpeningMessage(
                "${currentlySelectedUser!!.name} opened ${currentlySelectedDoor!!.name}.",
                R.string.green_color
            )
        } else {
            morphButton(
                R.string.red_color,
                R.drawable.ic_keyhole_to_error,
                R.drawable.ic_error_to_keyhole
            )
            showDoorOpeningMessage(
                "${currentlySelectedUser!!.name} tried to open ${currentlySelectedDoor!!.name}, but has no permission.",
                R.string.red_color
            )
        }
    }

    private fun morphButton(
        backgroundColorResource: Int,
        firstAvdResource: Int,
        secondAvdResource: Int
    ) {
        var backgroundColor = Color.parseColor(getString(backgroundColorResource))
        openDoorButton.setAvdFirst(
            AnimatedVectorDrawableCompat.create(
                context!!,
                firstAvdResource
            )!!
        )
        openDoorButton.setAvdSecond(
            AnimatedVectorDrawableCompat.create(
                context!!,
                secondAvdResource
            )!!
        )
        openDoorButton.backgroundTintList = ColorStateList.valueOf(backgroundColor)
        openDoorButton.morph()
        openDoorButton.isClickable = false

        Handler().postDelayed({
            backgroundColor = Color.parseColor(getString(R.string.blue_color))
            openDoorButton.backgroundTintList = ColorStateList.valueOf(backgroundColor)

            openDoorButton.morph()
            openDoorButton.isClickable = true
        }, 2000)
    }

    private fun showDoorOpeningMessage(message: String, color: Int) {
        TransitionManager.beginDelayedTransition(mainContainer)
        doorOpeningDescription.visibility = View.VISIBLE
        val textColor = Color.parseColor(getString(color))
        doorOpeningDescription.text = message
        doorOpeningDescription.setTextColor(ColorStateList.valueOf(textColor))

        Handler().postDelayed({
            doorOpeningDescription.visibility = View.INVISIBLE
        }, 2000)
    }

    override fun onDefaultError(error: String) {
        toastHelper.showToast(context!!, error)
    }

}
package com.victorlsn.salto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.victorlsn.salto.R
import com.victorlsn.salto.contracts.DoorsContract
import com.victorlsn.salto.data.models.Door
import com.victorlsn.salto.listeners.DoorSelectedListener
import com.victorlsn.salto.presenters.DoorsPresenter
import com.victorlsn.salto.ui.adapters.SimpleDoorAdapter
import kotlinx.android.synthetic.main.fragment_doors.*
import javax.inject.Inject

class DoorsFragment : BaseFragment(), DoorsContract.View {
    override fun resumeFragment() {}

    @Inject
    lateinit var presenter: DoorsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
        presenter.getDoors()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_doors, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAddDoorButton()
    }

    private fun setupAddDoorButton() {
        addDoorsButton.setOnClickListener {
            presenter.addNewDoor(nameInputLayout.editText?.text.toString())
        }
    }


    private fun setupRecyclerView(doors: ArrayList<Door>) {
        doorsRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = SimpleDoorAdapter(doors, object : DoorSelectedListener {
            override fun onDoorSelected(door: Door) {
                presenter.removeDoor(door)
            }

        })
        doorsRecyclerView.adapter = adapter
    }

    override fun onAddNewDoorSuccess() {
        nameInputLayout.editText?.setText("")
        nameInputLayout.error = null

        presenter.getDoors()
        Toast.makeText(context!!, "Door added successfully", Toast.LENGTH_SHORT).show()
    }

    override fun onAddNewDoorFailure(error: String) {
        nameInputLayout.error = error
    }

    override fun onRemoveDoorSuccess() {
        presenter.getDoors()
        Toast.makeText(context!!, "Door removed successfully", Toast.LENGTH_SHORT).show()
    }

    override fun onGetDoorsSuccess(doors: ArrayList<Door>) {
        setupRecyclerView(doors)
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
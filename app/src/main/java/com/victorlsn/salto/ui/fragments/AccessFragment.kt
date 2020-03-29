package com.victorlsn.salto.ui.fragments

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.victorlsn.salto.R
import com.victorlsn.salto.contracts.AccessContract
import com.victorlsn.salto.data.models.Door
import com.victorlsn.salto.data.models.Employee
import com.victorlsn.salto.listeners.DoorSelectedListener
import com.victorlsn.salto.listeners.EmployeeAuthorizedListener
import com.victorlsn.salto.presenters.AccessPresenter
import com.victorlsn.salto.ui.adapters.GridDoorAdapter
import com.victorlsn.salto.ui.adapters.GridEmployeeAdapter
import kotlinx.android.synthetic.main.fragment_access.*
import javax.inject.Inject

class AccessFragment : BaseFragment(), AccessContract.View {
    override fun resumeFragment() {
        presenter.getDoors()
        presenter.getEmployees()
    }

    @Inject
    lateinit var presenter: AccessPresenter
    private var doors: ArrayList<Door>? = null
    private var currentlySelectedDoor: Door? = null
    private var employees: ArrayList<Employee>? = null

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

    private fun setupDoorsRecyclerView(doors: ArrayList<Door>) {
        val gridDoorAdapter =
            GridDoorAdapter(doors,
                object : DoorSelectedListener {
                    override fun onDoorSelected(door: Door) {
                        currentlySelectedDoor = door
                        employeesTextView.visibility = View.VISIBLE
                        setupEmployeesRecyclerView()
                    }
                }
            )
        gridDoorAdapter.setHasStableIds(true)

        val layoutManager = GridLayoutManager(context, 3)

        doorsRecyclerView.layoutManager = layoutManager
        doorsRecyclerView.adapter = gridDoorAdapter

        doorsRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
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

//            private fun preselectPaymentMethod() {
//                if (paymentMethods.size == 1) {
//                    val holder = paymentMethodRecyclerView.findViewHolderForAdapterPosition(0)
//                    val itemView = holder?.itemView
//
//                    itemView?.performClick()
//                }
//                else {
//                    Prefs.getString("DEFAULT_PAYMENT_METHOD", null)?.let {
//                        val id = it.hashCode().toLong()
//                        val holder = paymentMethodRecyclerView.findViewHolderForItemId(id)
//                        val itemView = holder?.itemView
//
//                        itemView?.performClick()
//                    }
//                }
//            }
        })
    }

    override fun onGetEmployeesSuccess(employees: ArrayList<Employee>) {
        if (this.employees != employees) {
            this.employees = employees
        }
    }

    private fun setupEmployeesRecyclerView() {
        val gridEmployeeAdapter =
            GridEmployeeAdapter(currentlySelectedDoor!!, employees!!,
                object : EmployeeAuthorizedListener {
                    override fun onEmployeeAuthorizationChanged(
                        employee: Employee,
                        authorized: Boolean
                    ) {
                        presenter.changePermission(employee, currentlySelectedDoor!!, authorized)
                    }
                }
            )
        gridEmployeeAdapter.setHasStableIds(true)

        val layoutManager = GridLayoutManager(context, 4)

        employeesRecyclerView.layoutManager = layoutManager
        employeesRecyclerView.adapter = gridEmployeeAdapter
    }

    override fun onGetDoorsSuccess(doors: ArrayList<Door>) {
        if (this.doors != doors) {
            setupDoorsRecyclerView(doors)
        }
    }

    override fun onDefaultError(error: String) {
        Toast.makeText(context!!, error, Toast.LENGTH_LONG).show()
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
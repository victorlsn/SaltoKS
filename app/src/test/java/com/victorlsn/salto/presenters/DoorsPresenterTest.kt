package com.victorlsn.salto.presenters

import android.content.Context
import android.content.SharedPreferences
import com.github.ivanshafran.sharedpreferencesmock.SPMockBuilder
import com.nhaarman.mockitokotlin2.then
import com.victorlsn.salto.contracts.DoorsContract
import com.victorlsn.salto.data.Repository
import com.victorlsn.salto.data.models.Door
import com.victorlsn.salto.util.TrampolineSchedulerProvider
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class DoorsPresenterTest {

    private lateinit var presenter: DoorsPresenter
    private lateinit var repository: Repository
    private val spMockBuilder = SPMockBuilder()
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockView: DoorsContract.View

    private var schedulerProvider = TrampolineSchedulerProvider()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        sharedPreferences =
            spMockBuilder.createContext().getSharedPreferences("", Context.MODE_PRIVATE)
        repository =
            Repository(sharedPreferences)
        presenter = DoorsPresenter(schedulerProvider, repository)
        presenter.attachView(mockView)
    }

    @Test
    fun emptyGetDoorsTest() {
        presenter.getDoors()

        then(mockView).should().showLoading()
        then(mockView).should().hideLoading()

        then(mockView).should().onGetDoorsSuccess(ArrayList())
    }

    @Test
    fun populatedGetDoorsTest() {
        val door1 = Door("Front door")
        val door2 = Door("Storage door")
        val doorArrayList = arrayListOf(door1, door2)
        doorArrayList.sortBy { it.name }
        repository.addDoor(door1)
        repository.addDoor(door2)

        presenter.getDoors()

        then(mockView).should().showLoading()
        then(mockView).should().hideLoading()

        then(mockView).should().onGetDoorsSuccess(doorArrayList)
    }

    @Test
    fun addInvalidDoorTest() {
        presenter.addNewDoor("")

        then(mockView).should().showLoading()
        then(mockView).should().hideLoading()

        then(mockView).should().onAddNewDoorFailure("You can't add an empty-named door.")
    }

    @Test
    fun addValidDoorTest() {
        presenter.addNewDoor("Front door")

        then(mockView).should().showLoading()
        then(mockView).should().hideLoading()

        then(mockView).should().onAddNewDoorSuccess()
    }

    @Test
    fun addRepeatedDoorTest() {
        repository.addDoor(Door("Front door"))

        presenter.addNewDoor("Front door")

        then(mockView).should().showLoading()
        then(mockView).should().hideLoading()

        then(mockView).should().onAddNewDoorFailure("A door with that name already exists.")
    }

    @Test
    fun removeValidDoor() {
        repository.addDoor(Door("Front door"))

        presenter.removeDoor(Door("Front door"))

        then(mockView).should().showLoading()
        then(mockView).should().hideLoading()

        then(mockView).should().onRemoveDoorSuccess()
    }

    @Test
    fun removeInvalidDoor() {
        presenter.removeDoor(Door("Front door"))

        then(mockView).should().showLoading()
        then(mockView).should().hideLoading()

        then(mockView).should().onDefaultError("This door doesn't exist anymore.")
    }
}

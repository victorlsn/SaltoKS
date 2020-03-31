package com.victorlsn.salto.presenters

import android.content.Context
import android.content.SharedPreferences
import com.github.ivanshafran.sharedpreferencesmock.SPMockBuilder
import com.nhaarman.mockitokotlin2.then
import com.victorlsn.salto.contracts.AccessContract
import com.victorlsn.salto.contracts.DoorsContract
import com.victorlsn.salto.data.Repository
import com.victorlsn.salto.data.models.Door
import com.victorlsn.salto.data.models.User
import com.victorlsn.salto.util.TrampolineSchedulerProvider
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class AccessPresenterTest {

    private lateinit var presenter: AccessPresenter
    private lateinit var repository: Repository
    private val spMockBuilder = SPMockBuilder()
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockView: AccessContract.View

    private var schedulerProvider = TrampolineSchedulerProvider()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        sharedPreferences =
            spMockBuilder.createContext().getSharedPreferences("", Context.MODE_PRIVATE)
        repository =
            Repository(sharedPreferences)
        presenter = AccessPresenter(schedulerProvider, repository)
        presenter.attachView(mockView)
    }

    @Test
    fun emptyGetDoorsTest() {
        presenter.getDoors()

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

        then(mockView).should().onGetDoorsSuccess(doorArrayList)
    }

    @Test
    fun emptyGetUsersTest() {
        presenter.getUsers()

        then(mockView).should().onGetUsersSuccess(ArrayList())
    }

    @Test
    fun populatedGetUsersTest() {
        val user1 = User("Victor Negrão")
        val user2 = User("Aisha Santos")
        val userArrayList = arrayListOf(user1, user2)
        userArrayList.sortBy { it.name }
        repository.addUser(user1)
        repository.addUser(user2)

        presenter.getUsers()

        then(mockView).should().onGetUsersSuccess(userArrayList)
    }

    @Test
    fun changePermissionErrorTest() {
        val user = User("Victor Negrão")
        val door = Door("Front Door")

        presenter.changePermission(user, door, true)

        // Error because neither the user nor the door are stored
        then(mockView).should().onDefaultError("It was not possible to change permissions for this door/user.")
    }

    @Test
    fun changePermissionTest() {
        val user = User("Victor Negrão")
        val door = Door("Front Door")
        repository.addUser(user)
        repository.addDoor(door)

        var storedDoor = repository.getDoors().blockingSingle().first()
        assertEquals(false, storedDoor.isUserAuthorized(user))

        presenter.changePermission(user, door, true)
        storedDoor = repository.getDoors().blockingSingle().first()
        assertEquals(true, storedDoor.isUserAuthorized(user))

        presenter.changePermission(user, door, false)
        storedDoor = repository.getDoors().blockingSingle().first()
        assertEquals(false, storedDoor.isUserAuthorized(user))
    }
}

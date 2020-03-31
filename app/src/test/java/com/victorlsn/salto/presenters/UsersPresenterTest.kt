package com.victorlsn.salto.presenters

import android.content.Context
import android.content.SharedPreferences
import com.github.ivanshafran.sharedpreferencesmock.SPMockBuilder
import com.nhaarman.mockitokotlin2.then
import com.victorlsn.salto.contracts.UsersContract
import com.victorlsn.salto.data.Repository
import com.victorlsn.salto.data.models.User
import com.victorlsn.salto.util.TrampolineSchedulerProvider
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class UsersPresenterTest {

    private lateinit var presenter: UsersPresenter
    private lateinit var repository: Repository
    private val spMockBuilder = SPMockBuilder()
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockView: UsersContract.View

    private var schedulerProvider = TrampolineSchedulerProvider()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        sharedPreferences =
            spMockBuilder.createContext().getSharedPreferences("", Context.MODE_PRIVATE)
        repository =
            Repository(sharedPreferences)
        presenter = UsersPresenter(schedulerProvider, repository)
        presenter.attachView(mockView)
    }

    @Test
    fun emptyGetUsersTest() {
        presenter.getUsers()

        then(mockView).should().showLoading()
        then(mockView).should().hideLoading()

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

        then(mockView).should().showLoading()
        then(mockView).should().hideLoading()

        then(mockView).should().onGetUsersSuccess(userArrayList)
    }

    @Test
    fun addInvalidUserTest() {
        presenter.addNewUser("")

        then(mockView).should().showLoading()
        then(mockView).should().hideLoading()

        then(mockView).should().onAddNewUserFailure("You can't add an empty-named user.")
    }

    @Test
    fun addValidUserTest() {
        presenter.addNewUser("Front user")

        then(mockView).should().showLoading()
        then(mockView).should().hideLoading()

        then(mockView).should().onAddNewUserSuccess()
    }

    @Test
    fun addRepeatedUserTest() {
        repository.addUser(User("Victor Negrão"))

        presenter.addNewUser("Victor Negrão")

        then(mockView).should().showLoading()
        then(mockView).should().hideLoading()

        then(mockView).should().onAddNewUserFailure("A user with that name already exists.")
    }

    @Test
    fun removeValidUser() {
        repository.addUser(User("Victor Negrão"))

        presenter.removeUser(User("Victor Negrão"))

        then(mockView).should().showLoading()
        then(mockView).should().hideLoading()

        then(mockView).should().onRemoveUserSuccess()
    }

    @Test
    fun removeInvalidUser() {
        presenter.removeUser(User("Victor Negrão"))

        then(mockView).should().showLoading()
        then(mockView).should().hideLoading()

        then(mockView).should().onDefaultError("This user doesn't exist anymore.")
    }
}

package com.victorlsn.salto.presenters

import android.content.Context
import android.content.SharedPreferences
import com.github.ivanshafran.sharedpreferencesmock.SPMockBuilder
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.then
import com.victorlsn.salto.contracts.EventsContract
import com.victorlsn.salto.data.Repository
import com.victorlsn.salto.data.models.Door
import com.victorlsn.salto.data.models.User
import com.victorlsn.salto.util.TrampolineSchedulerProvider
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class EventsPresenterTest {

    private lateinit var presenter: EventsPresenter
    private lateinit var repository: Repository
    private val spMockBuilder = SPMockBuilder()
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockView: EventsContract.View

    private var schedulerProvider = TrampolineSchedulerProvider()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        sharedPreferences =
            spMockBuilder.createContext().getSharedPreferences("", Context.MODE_PRIVATE)
        repository =
            Repository(sharedPreferences)
        presenter = EventsPresenter(schedulerProvider, repository)
        presenter.attachView(mockView)
    }

    @Test
    fun emptyEventsErrorTest() {
        presenter.getEventLog()

        then(mockView).should().onDefaultError()
    }

    @Test
    fun getPopulatedEventsTest() {
        val user = User("Victor Negrão")
        val door = Door("Front Door")

        repository.addUser(user)
        repository.addDoor(door)
        repository.openDoor(door, user)

        presenter.getEventLog()
        val events = repository.getEventLog().blockingSingle()

        then(mockView).should().onEventLogRetrieved(any())
    }
}

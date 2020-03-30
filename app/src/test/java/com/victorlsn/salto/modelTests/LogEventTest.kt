package com.victorlsn.salto.modelTests

import com.victorlsn.salto.data.models.Door
import com.victorlsn.salto.data.models.LogEvent
import com.victorlsn.salto.data.models.User
import com.victorlsn.salto.util.extensions.getStringInFormat
import org.junit.Assert
import org.junit.Test
import java.util.*

class LogEventTest {

    private val user = User("Victor Negrão")
    private val door = Door("Front Door")
    private val date = Date()
    private val successEvent = LogEvent(user, door, true, date)
    private val failureEvent = LogEvent(user, door, false, date)

    @Test
    fun testLogEventMessage() {
        val successText = "${user.name} tried to open ${door.name} on ${date.getStringInFormat("dd/MM/yyyy HH:mm")} and was successful."
        Assert.assertEquals(successText, successEvent.getEventString())
        val failureText = "${user.name} tried to open ${door.name} on ${date.getStringInFormat("dd/MM/yyyy HH:mm")} but didn't have permission to do so."
        Assert.assertEquals(failureText, failureEvent.getEventString())
    }
}
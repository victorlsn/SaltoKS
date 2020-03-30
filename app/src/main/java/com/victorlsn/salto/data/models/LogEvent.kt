package com.victorlsn.salto.data.models

import com.victorlsn.salto.util.extensions.getStringInFormat
import java.io.Serializable
import java.util.*

class LogEvent(user: User, door: Door, private val success: Boolean, val date : Date = Date()) : Serializable {
    private val userName = user.name
    private val doorName = door.name

    fun getEventString() : String {
        var eventString = "$userName tried to open $doorName on ${date.getStringInFormat("dd/MM/yyyy HH:mm")}"
        eventString = if (success) {
            "$eventString and was successful."
        } else {
            "$eventString but didn't have permission to do so."
        }
        return eventString
    }
}
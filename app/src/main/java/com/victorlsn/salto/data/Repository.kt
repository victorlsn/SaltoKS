package com.victorlsn.salto.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pixplicity.easyprefs.library.Prefs
import com.victorlsn.salto.data.models.Door
import com.victorlsn.salto.data.models.LogEvent
import com.victorlsn.salto.data.models.User
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor() {

    // MARK : User-related methods

    fun getUsers() : Observable<ArrayList<User>> {
        val users = readUsers()
        return Observable.just(users)
    }

    fun addUser(newUser : User) : Observable<Boolean> {
        val users = readUsers()
        if (users.contains(newUser)) {
            return Observable.just(false)
        }
        users.add(newUser)

        updateUsers(users)

        return Observable.just(true)
    }

    fun removeUser(user: User) : Observable<Boolean> {
        val users = readUsers()
        if (users.contains(user)) {
            users.remove(user)

            updateUsers(users)

            return Observable.just(true)
        }
        return Observable.just(false)
    }

    private fun readUsers() : ArrayList<User> {
        val usersJson = Prefs.getString(USERS_KEY, null)

        usersJson?.let {
            val itemType = object : TypeToken<java.util.ArrayList<User>>() {}.type

            return Gson().fromJson(usersJson, itemType)
        }

        return ArrayList()
    }

    private fun updateUsers(users: ArrayList<User>) {
        users.sortBy { it.name }

        val usersJson = Gson().toJson(users)
        Prefs.putString(USERS_KEY, usersJson)
    }

    // MARK : Doors-related methods

    fun getDoors() : Observable<ArrayList<Door>> {
        val doors = readDoors()
        return Observable.just(doors)
    }

    fun addDoor(newDoor: Door) : Observable<Boolean> {
        val doors = readDoors()
        if (doors.contains(newDoor)) {
            return Observable.just(false)
        }
        doors.add(newDoor)

        updateDoorsJson(doors)

        return Observable.just(true)
    }

    fun removeDoor(door: Door) : Observable<Boolean> {
        val doors = readDoors()
        if (doors.contains(door)) {
            doors.remove(door)

            updateDoorsJson(doors)

            return Observable.just(true)
        }
        return Observable.just(false)
    }

    private fun readDoors() : ArrayList<Door> {
        val doorsJson = Prefs.getString(DOORS_KEY, null)

        doorsJson?.let {
            val itemType = object : TypeToken<java.util.ArrayList<Door>>() {}.type

            return Gson().fromJson(doorsJson, itemType)
        }

        return ArrayList()
    }

    private fun updateDoor(updatedDoor: Door) : Boolean {
        val doors = readDoors()
        for (door in doors) {
            if (door == updatedDoor) {
                doors.remove(door)
                doors.add(updatedDoor)

                updateDoorsJson(doors)

                return true
            }
        }

        return false
    }

    private fun updateDoorsJson(doors: ArrayList<Door>) {
        doors.sortBy { it.name }

        val doorsJson = Gson().toJson(doors)
        Prefs.putString(DOORS_KEY, doorsJson)
    }

    // MARK : Access-related methods

    fun changeUserPermissionForDoor(user: User, selectedDoor: Door, authorized: Boolean) : Observable<Boolean> {
        return if (authorized) {
            Observable.just(addPermissionForDoor(user, selectedDoor))
        } else {
            Observable.just(removePermissionForDoor(user, selectedDoor))
        }

    }

    private fun addPermissionForDoor(user: User, selectedDoor: Door) : Boolean {
        selectedDoor.addPermission(user)

        return updateDoor(selectedDoor)
    }

    private fun removePermissionForDoor(user: User, selectedDoor: Door) : Boolean {
        selectedDoor.removePermission(user)

        return updateDoor(selectedDoor)
    }

    // MARK : Door opening related methods

    fun openDoor(user: User, door: Door) : Observable<Boolean> {
        var success = false
        val doors = readDoors()
        for (actualDoor in doors) {
            if (door == actualDoor && actualDoor.isUserAuthorized(user)) {
                success = true
                break
            }
        }

        logEvent(user, door, success)
        return Observable.just(success)
    }

    // MARK : Log related methods

    fun getEventLog() : Observable<ArrayList<LogEvent>> {
        return Observable.just(readEventLog())
    }

    private fun logEvent(user: User, door: Door, success: Boolean) {
        val event = LogEvent(user, door, success)
        updateEventLog(event)
    }

    private fun updateEventLog(logEvent: LogEvent) {
        val logs = readEventLog()
        logs.add(logEvent)
        updateEventLogJson(logs)
    }

    private fun readEventLog() : ArrayList<LogEvent> {
        val logsJson = Prefs.getString(LOGS_KEY, null)

        logsJson?.let {
            val itemType = object : TypeToken<java.util.ArrayList<LogEvent>>() {}.type

            return Gson().fromJson(logsJson, itemType)
        }

        return ArrayList()
    }

    private fun updateEventLogJson(logs: ArrayList<LogEvent>) {
        logs.sortBy { it.date }

        val logsJson = Gson().toJson(logs)
        Prefs.putString(LOGS_KEY, logsJson)
    }


    companion object {
        private const val USERS_KEY = "USERS"
        private const val DOORS_KEY = "DOORS"
        private const val LOGS_KEY = "LOG"
    }
}
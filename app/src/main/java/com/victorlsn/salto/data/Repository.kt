package com.victorlsn.salto.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pixplicity.easyprefs.library.Prefs
import com.victorlsn.salto.data.models.Door
import com.victorlsn.salto.data.models.Employee
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor() {

    // MARK : Employee-related methods

    fun getEmployees() : Observable<ArrayList<Employee>> {
        val employees = readEmployees()
        return Observable.just(employees)
    }

    fun addEmployee(newEmployee : Employee) : Observable<Boolean> {
        val employees = readEmployees()
        if (employees.contains(newEmployee)) {
            return Observable.just(false)
        }
        employees.add(newEmployee)

        updateEmployees(employees)

        return Observable.just(true)
    }

    fun removeEmployee(employee: Employee) : Observable<Boolean> {
        val employees = readEmployees()
        if (employees.contains(employee)) {
            employees.remove(employee)

            updateEmployees(employees)

            return Observable.just(true)
        }
        return Observable.just(false)
    }

    private fun readEmployees() : ArrayList<Employee> {
        val employeesJson = Prefs.getString(EMPLOYEES_KEY, null)

        employeesJson?.let {
            val itemType = object : TypeToken<java.util.ArrayList<Employee>>() {}.type

            return Gson().fromJson(employeesJson, itemType)
        }

        return ArrayList()
    }

    private fun updateEmployees(employees: ArrayList<Employee>) {
        employees.sortBy { it.name }

        val employeesJson = Gson().toJson(employees)
        Prefs.putString(EMPLOYEES_KEY, employeesJson)
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

        updateDoors(doors)

        return Observable.just(true)
    }

    fun removeDoor(door: Door) : Observable<Boolean> {
        val doors = readDoors()
        if (doors.contains(door)) {
            doors.remove(door)

            updateDoors(doors)

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

    private fun updateDoors(doors: ArrayList<Door>) {
        doors.sortBy { it.name }

        val doorsJson = Gson().toJson(doors)
        Prefs.putString(DOORS_KEY, doorsJson)
    }

    companion object {
        private const val EMPLOYEES_KEY = "EMPLOYEES"
        private const val DOORS_KEY = "DOORS"
    }
}
package com.victorlsn.salto.data.models

import kotlinx.android.parcel.IgnoredOnParcel
import java.io.Serializable
import java.util.HashSet

class Door(var name: String) : Serializable {
    val id: Int = name.hashCode()
    private val authorizedPersonnel = HashSet<Int>()
    @Transient
    var isSelected = false

    fun addPermission(employee: Employee) {
        authorizedPersonnel.add(employee.id)
    }

    fun removePermission(employee: Employee) {
        authorizedPersonnel.remove(employee.id)
    }

    fun isUserAuthorized(employee: Employee) : Boolean {
        return authorizedPersonnel.contains(employee.id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Door

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}
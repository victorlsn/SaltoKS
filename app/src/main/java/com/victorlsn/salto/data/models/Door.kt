package com.victorlsn.salto.data.models

import java.io.Serializable
import java.util.*

class Door(val name: String) : Serializable {
    val id: Int = name.hashCode()
    private val authorizedPersonnel = HashSet<Int>()
    @Transient
    var isSelected = false

    fun addPermission(user: User) {
        authorizedPersonnel.add(user.id)
    }

    fun removePermission(user: User) {
        authorizedPersonnel.remove(user.id)
    }

    fun isUserAuthorized(user: User) : Boolean {
        return authorizedPersonnel.contains(user.id)
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
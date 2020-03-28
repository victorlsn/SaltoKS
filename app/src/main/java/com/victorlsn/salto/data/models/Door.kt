package com.victorlsn.salto.data.models

import java.io.Serializable
import java.util.HashSet

class Door(var name: String) : Serializable {
    val id: Int = name.hashCode()
    val authorizedPersonnel = HashSet<Employee>()

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
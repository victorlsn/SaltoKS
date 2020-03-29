package com.victorlsn.salto.data.models

class User(val name: String) {
    val id: Int = name.hashCode()
    @Transient
    var isSelected = false

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}
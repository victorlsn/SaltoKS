package com.victorlsn.salto.data.models

class Employee(var name: String) {
    val id: Int = name.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Employee

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}
package com.victorlsn.salto.listeners

import com.victorlsn.salto.data.models.Door

interface DoorSelectedListener {
    fun onDoorSelected(door: Door)
}
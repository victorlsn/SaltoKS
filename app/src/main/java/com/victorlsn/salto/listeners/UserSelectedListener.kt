package com.victorlsn.salto.listeners

import com.victorlsn.salto.data.models.User

interface UserSelectedListener {
   fun onUserSelected(user: User)
}
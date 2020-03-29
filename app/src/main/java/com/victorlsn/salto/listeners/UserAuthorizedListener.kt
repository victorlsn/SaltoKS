package com.victorlsn.salto.listeners

import com.victorlsn.salto.data.models.User

interface UserAuthorizedListener {
   fun onUserAuthorizationChanged(user: User, authorized: Boolean)
}
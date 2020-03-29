package com.victorlsn.salto.listeners

import com.victorlsn.salto.data.models.Employee

interface EmployeeAuthorizedListener {
   fun onEmployeeAuthorizationChanged(employee: Employee, authorized: Boolean)
}
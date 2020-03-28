package com.victorlsn.salto.listeners

import com.victorlsn.salto.data.models.Employee

interface EmployeeSelectedListener {
   fun onEmployeeSelected(employee: Employee)
}
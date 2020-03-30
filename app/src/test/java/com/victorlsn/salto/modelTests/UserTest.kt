package com.victorlsn.salto.modelTests

import com.victorlsn.salto.data.models.User
import org.junit.Assert

import org.junit.Test

class UserTest {

    private val user1 = User("Victor Negrão")
    private val user2 = User("Victor Negrao")
    private val user3 = User("Victor Negrão")

    @Test
    fun testUserEquals() {
        Assert.assertNotEquals(user1, user2)
        Assert.assertNotEquals(user2, user3)
        Assert.assertEquals(user1, user3)
    }
}
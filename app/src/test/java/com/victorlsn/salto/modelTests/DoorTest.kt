package com.victorlsn.salto.modelTests

import com.victorlsn.salto.data.models.Door
import com.victorlsn.salto.data.models.User
import org.junit.Assert

import org.junit.Test

class DoorTest {

    private val door1 = Door("Front door")
    private val door2 = Door("Back door")
    private val door3 = Door("Front door")
    private val user1 = User("Victor Negrão")
    private val user2 = User("Victor Negrao")

    @Test
    fun testDoorEquals() {
        Assert.assertNotEquals(door1, door2)
        Assert.assertNotEquals(door2, door3)
        Assert.assertEquals(door1, door3)
    }

    @Test
    fun testDoorsAddPermission() {
        Assert.assertFalse(door1.isUserAuthorized(user1))
        Assert.assertFalse(door1.isUserAuthorized(user2))
        door1.addPermission(user1)
        Assert.assertTrue(door1.isUserAuthorized(user1))
        Assert.assertFalse(door1.isUserAuthorized(user2))
    }

    @Test
    fun testDoorsRemovePermission() {
        Assert.assertFalse(door1.isUserAuthorized(user1))
        door1.addPermission(user1)
        Assert.assertTrue(door1.isUserAuthorized(user1))
        door1.removePermission(user1)
        Assert.assertFalse(door1.isUserAuthorized(user1))
    }
}
package com.example.campsiteapp

import org.junit.Before
import org.junit.Test

class UserTest{

    //These tests are unnecessary but whatever
    lateinit var testUser1: User
    lateinit var testUser2: User

    @Before
    fun setUp() {
        testUser1 = User()
        testUser2 = User()
    }

    @Test
    fun distinctUsersWithDefaultValues(){
        assert(testUser1!=testUser2)
    }

    @Test
    fun getUserName() {
        assert(testUser1.name=="")
        val userName=testUser1.name
        assert(userName=="")
    }

    @Test
    fun setUserName() {
        assert(testUser1.name=="")
        testUser1.name="Test"
        assert(testUser1.name=="Test")
    }

    @Test
    fun getUserEmail() {
        assert(testUser1.email=="")
        val userEmail=testUser1.email
        assert(userEmail=="")
    }

    @Test
    fun setUserEmail() {
        assert(testUser1.email=="")
        testUser1.email="Test"
        assert(testUser1.email=="Test")
    }

    @Test
    fun getUserPhoneNumber() {
        assert(testUser1.phone=="")
        val userPhoneNumber=testUser1.phone
        assert(userPhoneNumber=="")
    }

    @Test
    fun setUserPhoneNumber() {
        assert(testUser1.phone=="")
        testUser1.phone="Test"
        assert(testUser1.phone=="Test")
    }

    @Test
    fun getUserProfileImage() {
        assert(testUser1.profileImage=="")
        val userProfileImage=testUser1.profileImage
        assert(userProfileImage=="")
    }

    @Test
    fun setUserProfileImage() {
        assert(testUser1.profileImage=="")
        testUser1.profileImage="Test"
        assert(testUser1.profileImage=="Test")
    }

    @Test
    fun getUserDeviceId() {
        assert(testUser1.deviceId=="")
        val userDeviceId=testUser1.deviceId
        assert(userDeviceId=="")
    }

    @Test
    fun setUserDeviceId() {
        assert(testUser1.deviceId=="")
        testUser1.deviceId="Test"
        assert(testUser1.deviceId=="Test")
    }

    @Test
    fun getNotificationPreference() {
        assert(testUser1.notificationsEnabled)
        val notificationPreference=testUser1.notificationsEnabled
        assert(notificationPreference)
    }

    @Test
    fun setNotificationPreference() {
        assert(testUser1.notificationsEnabled)
        testUser1.notificationsEnabled=false
        assert(!testUser1.notificationsEnabled)
    }
}
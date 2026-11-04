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
        assert(testUser1.userName=="")
        val userName=testUser1.userName
        assert(userName=="")
    }

    @Test
    fun setUserName() {
        assert(testUser1.userName=="")
        testUser1.userName="Test"
        assert(testUser1.userName=="Test")
    }

    @Test
    fun getUserEmail() {
        assert(testUser1.userEmail=="")
        val userEmail=testUser1.userEmail
        assert(userEmail=="")
    }

    @Test
    fun setUserEmail() {
        assert(testUser1.userEmail=="")
        testUser1.userEmail="Test"
        assert(testUser1.userEmail=="Test")
    }

    @Test
    fun getUserPhoneNumber() {
        assert(testUser1.userPhoneNumber=="")
        val userPhoneNumber=testUser1.userPhoneNumber
        assert(userPhoneNumber=="")
    }

    @Test
    fun setUserPhoneNumber() {
        assert(testUser1.userPhoneNumber=="")
        testUser1.userPhoneNumber="Test"
        assert(testUser1.userPhoneNumber=="Test")
    }

    @Test
    fun getUserProfileImage() {
        assert(testUser1.userProfileImage=="")
        val userProfileImage=testUser1.userProfileImage
        assert(userProfileImage=="")
    }

    @Test
    fun setUserProfileImage() {
        assert(testUser1.userProfileImage=="")
        testUser1.userProfileImage="Test"
        assert(testUser1.userProfileImage=="Test")
    }

    @Test
    fun getUserDeviceId() {
        assert(testUser1.userDeviceId=="")
        val userDeviceId=testUser1.userDeviceId
        assert(userDeviceId=="")
    }

    @Test
    fun setUserDeviceId() {
        assert(testUser1.userDeviceId=="")
        testUser1.userDeviceId="Test"
        assert(testUser1.userDeviceId=="Test")
    }

    @Test
    fun getNotificationPreference() {
        assert(testUser1.notificationPreference)
        val notificationPreference=testUser1.notificationPreference
        assert(notificationPreference)
    }

    @Test
    fun setNotificationPreference() {
        assert(testUser1.notificationPreference)
        testUser1.notificationPreference=false
        assert(!testUser1.notificationPreference)
    }
}
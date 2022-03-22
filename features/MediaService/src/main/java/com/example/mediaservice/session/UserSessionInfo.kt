package com.example.mediaservice.session

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSessionInfo @Inject constructor() {
    var userId: Long = -1
    var deviceId: Long = -1

    fun create(userId: Long, deviceId: Long){
        this.userId = userId
        this.deviceId = deviceId
    }
}
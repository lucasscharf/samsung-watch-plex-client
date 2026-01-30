package com.plexwatch.data.local

import kotlinx.coroutines.flow.Flow

interface TokenStorageInterface {
    val authToken: Flow<String?>

    fun getAuthToken(): String?

    fun setAuthToken(token: String?)

    fun getUserId(): String?

    fun setUserId(userId: String?)

    fun getClientId(): String

    fun clear()
}

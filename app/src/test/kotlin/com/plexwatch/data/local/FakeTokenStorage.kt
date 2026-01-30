package com.plexwatch.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeTokenStorage : TokenStorageInterface {
    private val _authToken = MutableStateFlow<String?>(null)
    override val authToken: Flow<String?> = _authToken.asStateFlow()

    private var _userId: String? = null
    private var _clientId: String = "test-client-id"

    var setAuthTokenCalled = false
        private set
    var lastAuthToken: String? = null
        private set

    var setUserIdCalled = false
        private set
    var lastUserId: String? = null
        private set

    var clearCalled = false
        private set

    override fun getAuthToken(): String? = _authToken.value

    override fun setAuthToken(token: String?) {
        setAuthTokenCalled = true
        lastAuthToken = token
        _authToken.value = token
    }

    override fun getUserId(): String? = _userId

    override fun setUserId(userId: String?) {
        setUserIdCalled = true
        lastUserId = userId
        _userId = userId
    }

    override fun getClientId(): String = _clientId

    override fun clear() {
        clearCalled = true
        _authToken.value = null
        _userId = null
    }

    fun setClientIdForTest(clientId: String) {
        _clientId = clientId
    }
}

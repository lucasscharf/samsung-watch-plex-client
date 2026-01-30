package com.plexwatch.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenStorage
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : TokenStorageInterface {
        private val masterKey =
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

        private val prefs =
            EncryptedSharedPreferences.create(
                context,
                PREFS_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
            )

        private val _authToken = MutableStateFlow(prefs.getString(KEY_AUTH_TOKEN, null))
        override val authToken: Flow<String?> = _authToken.asStateFlow()

        override fun getAuthToken(): String? = prefs.getString(KEY_AUTH_TOKEN, null)

        override fun setAuthToken(token: String?) {
            prefs.edit().apply {
                if (token != null) {
                    putString(KEY_AUTH_TOKEN, token)
                } else {
                    remove(KEY_AUTH_TOKEN)
                }
            }.apply()
            _authToken.value = token
        }

        override fun getUserId(): String? = prefs.getString(KEY_USER_ID, null)

        override fun setUserId(userId: String?) {
            prefs.edit().apply {
                if (userId != null) {
                    putString(KEY_USER_ID, userId)
                } else {
                    remove(KEY_USER_ID)
                }
            }.apply()
        }

        override fun getClientId(): String {
            var clientId = prefs.getString(KEY_CLIENT_ID, null)
            if (clientId == null) {
                clientId = java.util.UUID.randomUUID().toString()
                prefs.edit().putString(KEY_CLIENT_ID, clientId).apply()
            }
            return clientId
        }

        override fun clear() {
            prefs.edit().clear().apply()
            _authToken.value = null
        }

        companion object {
            private const val PREFS_NAME = "plex_secure_prefs"
            private const val KEY_AUTH_TOKEN = "auth_token"
            private const val KEY_USER_ID = "user_id"
            private const val KEY_CLIENT_ID = "client_id"
        }
    }

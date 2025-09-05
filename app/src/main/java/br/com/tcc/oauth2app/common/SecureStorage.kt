package br.com.tcc.oauth2app.common

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.core.content.edit
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecureStorage @Inject constructor(private val context: Context) {

    private val prefs by lazy { getSafeEncryptedSharedPreferences(context) }

    companion object {
        private const val PREFS_NAME = "secure_prefs"
        private const val TOKEN_KEY = "auth_token"
        private const val EXPIRATION_KEY = "token_expiration"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
        private const val CODE_CHALLENGE_KEY = "code_challenge"
        private const val CODE_VERIFIER_KEY = "code_verifier"
    }

    private fun getMasterKey(context: Context): MasterKey {
        return MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    private fun getEncryptedSharedPreferences(context: Context): android.content.SharedPreferences {
        return EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            getMasterKey(context),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun getSafeEncryptedSharedPreferences(context: Context): android.content.SharedPreferences {
        return try {
            getEncryptedSharedPreferences(context)
        } catch (e: Exception) {
            // Preferências corrompidas, apaga e recria
            context.deleteSharedPreferences(PREFS_NAME)
            getEncryptedSharedPreferences(context)
        }
    }

    fun saveToken(token: String) {
        prefs.edit {
            putString(TOKEN_KEY, token)
        }
    }

    fun getToken(): String? {
        return prefs.getString(TOKEN_KEY, null)
    }

    fun saveTokenWithExpiration(token: String, expiresInSeconds: Long) {
        Timber.d("Saving token with expiration: $expiresInSeconds")
        val expirationTime = System.currentTimeMillis() + (expiresInSeconds * 1000)
        Timber.d("Calculated expiration time: $expirationTime")
        prefs.edit {
            putString(TOKEN_KEY, token)
                .putLong(EXPIRATION_KEY, expirationTime)
        }
    }

    fun saveRefreshToken(refreshToken: String) {
        prefs.edit {
            putString(REFRESH_TOKEN_KEY, refreshToken)
        }
    }

    fun getRefreshToken(): String? {
        return prefs.getString(REFRESH_TOKEN_KEY, null)
    }

    fun isTokenExpired(): Boolean {
        val expirationTime = prefs.getLong(EXPIRATION_KEY, 0L)
        return System.currentTimeMillis() > expirationTime
    }

    fun clearTokens() {
        prefs.edit {
            remove(TOKEN_KEY)
                .remove(REFRESH_TOKEN_KEY)
                .remove(EXPIRATION_KEY)
        }
    }

    fun saveCodeChallenge(codeChallenge: String) {
        prefs.edit {
            putString(CODE_CHALLENGE_KEY, codeChallenge)
        }
    }

    fun saveCodeVerifier(codeVerifier: String) {
        prefs.edit {
            putString(CODE_VERIFIER_KEY, codeVerifier)
        }
    }

    fun getCodeChallenge(): String? {
        return prefs.getString(CODE_CHALLENGE_KEY, null)
    }

    fun getCodeVerifier(): String? {
        return prefs.getString(CODE_VERIFIER_KEY, null)
    }
}
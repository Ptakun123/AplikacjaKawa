package com.example.myapplication.data

import android.content.Context
import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "secure_settings")

class TokenManager(private val context: Context) {

    private val aead: Aead

    init {
        AeadConfig.register()

        val keysetHandle = AndroidKeysetManager.Builder()
            .withSharedPref(context, "tink_keyset", "secure_keys")
            .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
            .withMasterKeyUri("android-keystore://master_key_tink")
            .build()
            .keysetHandle

        aead = keysetHandle.getPrimitive(Aead::class.java)
    }

    companion object {
        private val JWT_TOKEN_KEY = stringPreferencesKey("jwt_token_encrypted")
    }

    suspend fun saveToken(token: String) {
        // Szyfrowanie surowego ciągu i kodowanie do Base64 dla zapisu do Stringa
        val encryptedToken = aead.encrypt(token.toByteArray(Charsets.UTF_8), null)
        val encodedToken = Base64.encodeToString(encryptedToken, Base64.NO_WRAP)

        context.dataStore.edit { preferences ->
            preferences[JWT_TOKEN_KEY] = encodedToken
        }
    }

    fun getToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[JWT_TOKEN_KEY]?.let { encodedToken ->
                try {
                    val decodedToken = Base64.decode(encodedToken, Base64.NO_WRAP)
                    val decryptedToken = aead.decrypt(decodedToken, null)
                    String(decryptedToken, Charsets.UTF_8)
                } catch (e: Exception) {
                    null
                }
            }
        }
    }

    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(JWT_TOKEN_KEY)
        }
    }
}
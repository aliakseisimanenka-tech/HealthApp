package com.lsimanenka.shared.datastore

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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "health_app_auth_prefs")

class TokenManagerImpl(private val context: Context) : TokenManager {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("user_token_enc")
        private val USER_ID_KEY = stringPreferencesKey("user_id_enc")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email_enc")
    }

    private val aead: Aead

    init {
        AeadConfig.register()

        val keysetHandle = AndroidKeysetManager.Builder()
            .withSharedPref(context, "auth_keyset", "auth_keyset_prefs")
            .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
            .withMasterKeyUri("android-keystore://health_app_master_key")
            .build()
            .keysetHandle

        aead = keysetHandle.getPrimitive(Aead::class.java)
    }

    private fun encrypt(data: String): String {
        val ciphertext = aead.encrypt(data.toByteArray(Charsets.UTF_8), null)
        return Base64.encodeToString(ciphertext, Base64.NO_WRAP)
    }

    private fun decrypt(encryptedData: String?): String? {
        if (encryptedData == null) return null
        return try {
            val decoded = Base64.decode(encryptedData, Base64.NO_WRAP)
            val plaintext = aead.decrypt(decoded, null)
            String(plaintext, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override val userIdFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        decrypt(prefs[USER_ID_KEY])
    }

    override fun getToken(): String? = runBlocking(Dispatchers.IO) {
        decrypt(context.dataStore.data.first()[TOKEN_KEY])
    }

    override fun getUserId(): String? = runBlocking(Dispatchers.IO) {
        decrypt(context.dataStore.data.first()[USER_ID_KEY])
    }

    override fun getUserEmail(): String? = runBlocking(Dispatchers.IO) {
        decrypt(context.dataStore.data.first()[USER_EMAIL_KEY])
    }

    override fun saveAuthData(token: String, userId: String, userEmail: String) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit { prefs ->
                prefs[TOKEN_KEY] = encrypt(token)
                prefs[USER_ID_KEY] = encrypt(userId)
                prefs[USER_EMAIL_KEY] = encrypt(userEmail)
            }
        }
    }

    override fun clear() {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit { prefs ->
                prefs.clear()
            }
        }
    }
}
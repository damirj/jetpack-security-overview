package com.example.jetpacksecurityoverview

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class EncryptedPrefs(masterKey: MasterKey) : EncryptedPrefsInterface {
  
  private val encryptedSharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
    App.instance,
    SHARED_PREFS_FILENAME,
    masterKey,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
  )
  
  override fun savePassword(password: String) {
    encryptedSharedPreferences.edit().putString(PASSWORD_KEY, password).apply()
  }
  
  override fun getPassword(): String {
    return encryptedSharedPreferences.getString(PASSWORD_KEY, EMPTY_STRING) ?: EMPTY_STRING
  }
  
  override fun deletePassword() {
    encryptedSharedPreferences.edit().putString(PASSWORD_KEY, EMPTY_STRING).apply()
  }
  
  companion object {
    private const val PASSWORD_KEY = "PASSWORD"
    private const val SHARED_PREFS_FILENAME = "sharedPrefs"
  }
}
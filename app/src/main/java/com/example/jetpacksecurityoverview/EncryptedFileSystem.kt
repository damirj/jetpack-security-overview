package com.example.jetpacksecurityoverview

import android.util.Log
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets


class EncryptedFileSystem(masterKey: MasterKey) : EncryptedFileSystemInterface {
  
  private val encryptedFile = EncryptedFile.Builder(
    App.instance,
    File(App.instance.filesDir, FILE_NAME),
    masterKey,
    EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
  ).build()
  
  
  override fun savePassword(password: String) {
    deletePassword()
    encryptedFile.openFileOutput().apply {
      write(password.toByteArray(StandardCharsets.UTF_8))
      flush()
      close()
    }
  }
  
  override fun getPassword(): String {
    var password = EMPTY_STRING
    try {
      val inputStream = encryptedFile.openFileInput()
      val byteArrayOutputStream = ByteArrayOutputStream()
      var nextByte: Int = inputStream.read()
      while (nextByte != -1) {
        byteArrayOutputStream.write(nextByte)
        nextByte = inputStream.read()
      }
      password = byteArrayOutputStream.toString()
      return password
    } catch (exception: IOException) {
      Log.d(LOG_EXCEPTION_TAG, exception.message ?: EMPTY_STRING)
    }
    return password
  }
  
  override fun deletePassword() {
    val file = File(App.instance.filesDir, FILE_NAME)
    if (file.exists()) file.delete()
  }
  
  
  companion object {
    private const val FILE_NAME = "secret_file.txt"
    private const val LOG_EXCEPTION_TAG = "Read file exception"
  }
}
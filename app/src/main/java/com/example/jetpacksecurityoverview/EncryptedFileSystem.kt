package com.example.jetpacksecurityoverview

import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import java.io.File
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
    val bufferReader = encryptedFile.openFileInput().bufferedReader()
    val password = bufferReader.readText()
    bufferReader.close()
    
    return password
  }
  
  override fun deletePassword() {
    val file = File(App.instance.filesDir, FILE_NAME)
    if (file.exists()) file.delete()
  }
  
  
  companion object {
    private const val FILE_NAME = "secret_file.txt"
  }
}
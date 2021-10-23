package com.example.jetpacksecurityoverview

interface EncryptedFileSystemInterface {
  
  fun savePassword(password: String)
  
  fun getPassword(): String
  
  fun deletePassword()
}
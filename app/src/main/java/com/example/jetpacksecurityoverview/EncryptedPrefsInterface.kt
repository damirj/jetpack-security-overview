package com.example.jetpacksecurityoverview

interface EncryptedPrefsInterface {
  
  fun savePassword(password: String)
  
  fun getPassword(): String
}
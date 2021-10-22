package com.example.jetpacksecurityoverview

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.MasterKey
import com.example.jetpacksecurityoverview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding
  
  private val masterKey: MasterKey by lazy { generateMasterKey() }
  private val encryptedPrefs: EncryptedPrefsInterface by lazy { EncryptedPrefs(masterKey) }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)
    
    initUI()
    setupListeners()
  }
  
  private fun initUI() {
    if (getPasswordFromEncryptedPrefs().isBlank()) {
      binding.sharedPrefsPassword.text = ""
    } else {
      hidePrefsPassword()
    }
  }
  
  private fun setupListeners() {
    binding.run {
      sharedPrefsSubmit.setOnClickListener { savePasswordToEncryptedPrefs() }
      filesSubmit.setOnClickListener { savePasswordToEncryptedFile() }
      showSharedPrefsPassword.setOnClickListener { toggleSharedPrefsPasswordVisibility() }
      showFilesPassword.setOnClickListener { toggleFilesPasswordVisibility() }
      deleteSharedPrefsPassword.setOnClickListener { deleteSharedPrefsPassword() }
      deleteFilePassword.setOnClickListener { deleteFilePassword() }
    }
  }
  
  private fun generateMasterKey(): MasterKey {
    return MasterKey.Builder(this).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
  }
  
  private fun savePasswordToEncryptedPrefs() {
    closeKeyboard()
    if (isInputValid(binding.sharedPrefsPasswordInput.text.toString())) {
      encryptedPrefs.savePassword(binding.sharedPrefsPasswordInput.text.toString())
      binding.sharedPrefsPassword.text = getText(R.string.hidden_password)
      binding.showSharedPrefsPassword.text = getText(R.string.show_password)
      binding.sharedPrefsPasswordInput.text.clear()
      showToast("Password saved successfully!")
    } else {
      showToast("Your input is invalid, please try again!")
    }
  }
  
  private fun savePasswordToEncryptedFile() {
    if (isInputValid(binding.sharedPrefsPasswordInput.text.toString())) {
      TODO("Not yet implemented")
    } else {
      showToast("Your input is invalid, please try again!")
    }
  }
  
  private fun toggleSharedPrefsPasswordVisibility() {
    if (binding.showSharedPrefsPassword.text == getText(R.string.show_password)) {
      if (binding.sharedPrefsPassword.text != "") {
        binding.showSharedPrefsPassword.text = getText(R.string.hide_password)
        showSharedPrefsPassword()
      } else {
        showToast("Password is not set!")
      }
    } else {
      binding.showSharedPrefsPassword.text = getText(R.string.show_password)
      hidePrefsPassword()
    }
  }
  
  private fun showSharedPrefsPassword() {
    val password = getPasswordFromEncryptedPrefs()
    if (password.isBlank()) {
      binding.sharedPrefsPassword.text = ""
      showToast("Password is not set!")
    } else {
      binding.sharedPrefsPassword.text = password
    }
  }
  
  private fun hidePrefsPassword() {
    binding.sharedPrefsPassword.text = getText(R.string.hidden_password)
  }
  
  private fun toggleFilesPasswordVisibility() {
    if (binding.showFilesPassword.text == getText(R.string.show_password)) {
      binding.showFilesPassword.text = getText(R.string.hide_password)
      showFilesPassword()
    } else {
      binding.showFilesPassword.text = getText(R.string.show_password)
      hideFilesPassword()
    }
  }
  
  private fun showFilesPassword() {
    val password = getPasswordFromEncryptedFile()
    if (password.isBlank()) {
      binding.filesPassword.text = ""
      showToast("Password is not set!")
    } else {
      binding.filesPassword.text = password
    }
  }
  
  private fun hideFilesPassword() {
    binding.filesPassword.text = getText(R.string.hidden_password)
  }
  
  private fun getPasswordFromEncryptedPrefs(): String {
    return encryptedPrefs.getPassword()
  }
  
  private fun getPasswordFromEncryptedFile(): String {
    TODO("Not yet implemented")
  }
  
  private fun deleteSharedPrefsPassword() {
    if (binding.sharedPrefsPassword.text.isNotBlank()) {
      encryptedPrefs.savePassword("")
      binding.sharedPrefsPassword.text = ""
      binding.showSharedPrefsPassword.text = getText(R.string.show_password)
      showToast("Password deleted successfully!")
    } else {
      showToast("Password is not set!")
    }
  }
  
  private fun deleteFilePassword() {
    if (binding.filesPassword.text.isNotBlank()) {
      TODO()
      showToast("Password deleted successfully!")
    } else {
      showToast("Password is not set!")
    }
  }
  
  private fun isInputValid(input: String): Boolean {
    return input.isNotBlank()
  }
  
  private fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
  }
  
  private fun closeKeyboard() {
    val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
  }
  
}
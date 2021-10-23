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
  private val encryptedFile: EncryptedFileSystem by lazy { EncryptedFileSystem(masterKey) }
  
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
      binding.sharedPrefsPassword.text = EMPTY_STRING
    } else {
      hidePrefsPassword()
    }
    if (getPasswordFromEncryptedFile().isBlank()) {
      binding.filesPassword.text = EMPTY_STRING
    } else {
      hideFilesPassword()
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
      showToast(getString(R.string.password_saved))
    } else {
      showToast(getString(R.string.input_invalid))
    }
  }
  
  private fun savePasswordToEncryptedFile() {
    closeKeyboard()
    if (isInputValid(binding.filesPasswordInput.text.toString())) {
      encryptedFile.savePassword(binding.filesPasswordInput.text.toString())
      binding.filesPassword.text = getText(R.string.hidden_password)
      binding.showFilesPassword.text = getText(R.string.show_password)
      binding.filesPasswordInput.text.clear()
      showToast(getString(R.string.password_saved))
    } else {
      showToast(getString(R.string.input_invalid))
    }
  }
  
  private fun toggleSharedPrefsPasswordVisibility() {
    if (binding.showSharedPrefsPassword.text == getText(R.string.show_password)) {
      if (binding.sharedPrefsPassword.text != EMPTY_STRING) {
        binding.showSharedPrefsPassword.text = getText(R.string.hide_password)
        showSharedPrefsPassword()
      } else {
        showToast(getString(R.string.password_not_set))
      }
    } else {
      binding.showSharedPrefsPassword.text = getText(R.string.show_password)
      hidePrefsPassword()
    }
  }
  
  private fun showSharedPrefsPassword() {
    val password = getPasswordFromEncryptedPrefs()
    if (password.isBlank()) {
      binding.sharedPrefsPassword.text = EMPTY_STRING
      showToast(getString(R.string.password_not_set))
    } else {
      binding.sharedPrefsPassword.text = password
    }
  }
  
  private fun hidePrefsPassword() {
    binding.sharedPrefsPassword.text = getText(R.string.hidden_password)
  }
  
  private fun toggleFilesPasswordVisibility() {
    if (binding.showFilesPassword.text == getText(R.string.show_password)) {
      if (binding.filesPassword.text != EMPTY_STRING) {
        binding.showFilesPassword.text = getText(R.string.hide_password)
        showFilesPassword()
      } else {
        showToast(getString(R.string.password_not_set))
      }
    } else {
      binding.showFilesPassword.text = getText(R.string.show_password)
      hideFilesPassword()
    }
  }
  
  private fun showFilesPassword() {
    val password = getPasswordFromEncryptedFile()
    if (password.isBlank()) {
      binding.filesPassword.text = ""
      showToast(getString(R.string.password_not_set))
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
    return encryptedFile.getPassword()
  }
  
  private fun deleteSharedPrefsPassword() {
    if (binding.sharedPrefsPassword.text.isNotBlank()) {
      encryptedPrefs.deletePassword()
      binding.sharedPrefsPassword.text = EMPTY_STRING
      binding.showSharedPrefsPassword.text = getText(R.string.show_password)
      showToast(getString(R.string.password_deleted))
    } else {
      showToast(getString(R.string.password_not_set))
    }
  }
  
  private fun deleteFilePassword() {
    if (binding.filesPassword.text.isNotBlank()) {
      encryptedFile.deletePassword()
      binding.filesPassword.text = EMPTY_STRING
      binding.showFilesPassword.text = getText(R.string.show_password)
      showToast(getString(R.string.password_deleted))
    } else {
      showToast(getString(R.string.password_not_set))
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

const val EMPTY_STRING = ""
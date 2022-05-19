package com.tyron.talkrr.ui.login

import androidx.core.util.PatternsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.tyron.talkrr.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _usernameErrorMessage = MutableLiveData("")
    val usernameErrorMessage: LiveData<String> = _usernameErrorMessage

    private val _passwordErrorMessage = MutableLiveData("")
    val passwordErrorMessage: LiveData<String> = _passwordErrorMessage

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun login(email: String, password: String) {
        _usernameErrorMessage.value = ""
        _passwordErrorMessage.value = ""

        if (email.isEmpty()) {
            _usernameErrorMessage.value = "Username cannot be empty."
            return
        }

        if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            _usernameErrorMessage.value = "Not a valid email address."
            return
        }

        if (password.isEmpty()) {
            _passwordErrorMessage.value = "Password cannot be empty."
            return
        }

        _loading.value = true
        viewModelScope.launch {
            val result = runCatching { authRepository.loginWithEmailAndPassword(email, password) }
            result.onFailure {
                when (it) {
                    is FirebaseAuthInvalidCredentialsException ->
                        _usernameErrorMessage.value = it.message
                    else ->
                        _passwordErrorMessage.value = it.message
                }
            }
        }.invokeOnCompletion {
            _loading.value = false
        }
    }
}
package com.dong.circlelive.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dong.circlelive.IM
import com.dong.circlelive.R
import com.dong.circlelive.base.Timber
import com.dong.circlelive.login
import com.dong.circlelive.model.createAVUser
import com.dong.circlelive.model.login
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginViewModel : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val result =  withContext(Dispatchers.IO) {
                    try {
                        val avUser = createAVUser(username, password).login()
                        IM.login(createAVUser(username, password))
                    } catch (t: Throwable) {
                        Timber.e(t) { "login error" }
                        LoginResult(error = t.message)
                    }
                }
                _loginResult.value = result
            } catch (t: Throwable) {
                Timber.e(t) { "login error" }
                _loginResult.value = LoginResult(error = t.message)
            }
        }

    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return username.trim().isNotEmpty()
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
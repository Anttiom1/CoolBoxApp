package com.example.androidcoolboxryhma2.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcoolboxryhma2.model.LoginState
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _loginState = mutableStateOf(LoginState())
    val loginState: State<LoginState> = _loginState

    fun setUsername(newUsername: String){
        _loginState.value = _loginState.value.copy(username = newUsername)
    }

    fun setPassword(newPassword: String){
        _loginState.value = _loginState.value.copy(password = newPassword)
    }

    fun login(){
        viewModelScope.launch {
            try {
                _loginState.value = _loginState.value.copy(loading=true)
                /******** LOGIN ************/
            } catch (e: Exception) {
                _loginState.value = _loginState.value.copy(error = e.toString())
            } finally {
                _loginState.value = _loginState.value.copy(loading=false)
            }
        }
    }
}
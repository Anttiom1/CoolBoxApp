package com.example.androidcoolboxryhma2.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcoolboxryhma2.AccountDatabase
import com.example.androidcoolboxryhma2.AccountEntity
import com.example.androidcoolboxryhma2.DbProvider
import com.example.androidcoolboxryhma2.api.authService
import com.example.androidcoolboxryhma2.model.AuthReq
import com.example.androidcoolboxryhma2.model.LoginState
import kotlinx.coroutines.launch

class LoginViewModel(private val db: AccountDatabase = DbProvider.db) : ViewModel() {
    private val _loginState = mutableStateOf(LoginState())
    val loginState: State<LoginState> = _loginState

    fun setUsername(newUsername: String){
        _loginState.value = _loginState.value.copy(username = newUsername)
    }

    fun setPassword(newPassword: String){
        _loginState.value = _loginState.value.copy(password = newPassword)
    }
    fun setLogin(done: Boolean) {
        _loginState.value = _loginState.value.copy(loginDone = done)
    }

    fun login(){
        viewModelScope.launch {
            try {
                _loginState.value = _loginState.value.copy(loading=true)
                val res = authService.login(
                    AuthReq(
                        username = _loginState.value.username,
                        password = _loginState.value.password
                    )
                )
                db.accountDao().addToken(
                    AccountEntity(accessToken = res.token)
                )
                setLogin(true)
            } catch (e: Exception) {
                _loginState.value = _loginState.value.copy(error = e.toString())
            } finally {
                _loginState.value = _loginState.value.copy(loading=false)
            }
        }
    }
}
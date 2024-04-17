package com.example.androidcoolboxryhma2.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcoolboxryhma2.AccountDatabase
import com.example.androidcoolboxryhma2.DbProvider
import com.example.androidcoolboxryhma2.api.authService
import com.example.androidcoolboxryhma2.model.HomeState
import com.example.androidcoolboxryhma2.model.LogoutState
import kotlinx.coroutines.launch

class HomeViewModel(private val db: AccountDatabase = DbProvider.db): ViewModel() {
    private val _homeState = mutableStateOf(HomeState())
    val homeState: State<HomeState> = _homeState
    private val _logoutState = mutableStateOf(LogoutState())
    val logoutState: State<LogoutState> = _logoutState


    fun setLogout(done: Boolean){
        _logoutState.value = _logoutState.value.copy(logoutDone = done)
    }
    fun setConfirmLogout(logout: Boolean){
        _logoutState.value = _logoutState.value.copy(logout=logout)
    }
    fun logout() {
        viewModelScope.launch {
            try {
                _logoutState.value = _logoutState.value.copy(loading = true)
                val accessToken = db.accountDao().getToken()
                accessToken?.let {
                    //authService.logout("Bearer $it")
                    db.accountDao().removeTokens()
                    setLogout(true)
                }
            } catch (e: Exception) {
                _logoutState.value = _logoutState.value.copy(error = e.toString())
            } finally {
                _logoutState.value = _logoutState.value.copy(loading = false)
                setConfirmLogout(false)
            }
        }

    }
}
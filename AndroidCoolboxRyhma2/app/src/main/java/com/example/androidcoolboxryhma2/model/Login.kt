package com.example.androidcoolboxryhma2.model

import com.google.gson.annotations.SerializedName

data class LoginState(
    val username: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val loginDone: Boolean = false
)
data class LogoutState(
    val loading: Boolean = false,
    val error: String? = null,
    val logout: Boolean = false,
    val logoutDone: Boolean = false
)

data class AuthReq(val username: String = "", val password: String = "")
data class AuthRes(
    val token: String = ""
)
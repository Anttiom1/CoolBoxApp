package com.example.androidcoolboxryhma2.model

data class LoginState(
    val username: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null
)
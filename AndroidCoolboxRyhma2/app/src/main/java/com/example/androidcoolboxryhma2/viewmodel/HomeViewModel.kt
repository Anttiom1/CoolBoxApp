package com.example.androidcoolboxryhma2.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.androidcoolboxryhma2.model.HomeState

class HomeViewModel: ViewModel() {
    private val _homeState = mutableStateOf(HomeState())
    val homeState: State<HomeState> = _homeState
}
package com.example.androidcoolboxryhma2.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.androidcoolboxryhma2.model.TemperaturesState
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.androidcoolboxryhma2.api.temperatureService
import kotlinx.coroutines.launch

class GraphTestViewModel: ViewModel() {
    private val _temperaturesState = mutableStateOf(TemperaturesState())
    val temperatureState: State<TemperaturesState> = _temperaturesState

    // alustetaan listaan kaksi arvoa jotta line chart voidaan näyttää viiva arvojen välille
    /*init {
        getLatestTemperatureIndoors()
        getLatestTemperatureIndoors()
    }*/
    fun getLatestTemperatureIndoors(){
        viewModelScope.launch {
            try {
                _temperaturesState.value = _temperaturesState.value.copy(loading = true)
                val res = temperatureService.getLatestTemperatureTest(14)
                _temperaturesState.value = _temperaturesState.value.copy(list = _temperaturesState.value.list + res.data)
                Log.d("antti", res.toString())

            }
            catch (e: Exception){
                _temperaturesState.value = _temperaturesState.value.copy(error = e.toString())
                Log.d("antti", e.toString())
            }
            finally {
                _temperaturesState.value = _temperaturesState.value.copy(loading = false)
            }
        }
    }
}
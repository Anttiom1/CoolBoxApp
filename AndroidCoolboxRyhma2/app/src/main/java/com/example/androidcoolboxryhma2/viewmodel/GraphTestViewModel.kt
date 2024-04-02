package com.example.androidcoolboxryhma2.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.androidcoolboxryhma2.model.TemperaturesState
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.androidcoolboxryhma2.api.temperatureService
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class GraphTestViewModel: ViewModel() {
    private val _temperaturesState = mutableStateOf(TemperaturesState())
    val temperatureState: State<TemperaturesState> = _temperaturesState

    var month: Int = 0
    var day: Int = 0

    // alustetaan listaan kaksi arvoa jotta line chart voidaan näyttää viiva arvojen välille
    /*init {
        getLatestTemperatureIndoors()
        getLatestTemperatureIndoors()
    }*/

    fun getDailyAverageTemperature(){
        viewModelScope.launch {
            try {
                _temperaturesState.value = _temperaturesState.value.copy(loading = true)
                val res = temperatureService.getDailyAverageTemperature(month, day)
                _temperaturesState.value = _temperaturesState.value.copy(list = res.data)
                //Log.d("antti", res.toString())
                Log.d("antti", _temperaturesState.value.list.toString())

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

    fun calculateDate(milSecs: Long?,){
        val date = milSecs?.let { Date(it) }
        val calendar = Calendar.getInstance().apply {
            time = date
        }

        month = calendar.get(Calendar.MONTH) + 1
        day = calendar.get(Calendar.DAY_OF_MONTH)
    }
}
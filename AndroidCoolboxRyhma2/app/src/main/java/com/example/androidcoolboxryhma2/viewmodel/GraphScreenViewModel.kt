package com.example.androidcoolboxryhma2.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcoolboxryhma2.api.energyService
import com.example.androidcoolboxryhma2.api.temperatureService
import com.example.androidcoolboxryhma2.model.EnergyState
import com.example.androidcoolboxryhma2.model.TemperaturesState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import java.util.Date
import java.util.Locale

class GraphScreenViewModel: ViewModel() {
    private val _temperaturesState = mutableStateOf(TemperaturesState())
    val temperatureState: State<TemperaturesState> = _temperaturesState
    private val _energyState = mutableStateOf(EnergyState())
    val energyState: State<EnergyState> = _energyState
    private val _outdoorTemperatureState = mutableStateOf<TemperaturesState?>(null)
    val outdoorTemperatureState: State<TemperaturesState?> = _outdoorTemperatureState


    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    private var dateString: String = ""
    var initialTime: Long = 0
    fun getDateString(): String{
        return dateString
    }
    private fun setDateString(date: String){
        dateString = date
    }

    init {
        initialTime = Instant.now().toEpochMilli()
        calculateDate(initialTime)
        getDailyAverageTemperature()
    }

    fun getDailyEnergyConsumption(){
        viewModelScope.launch {
            try {
                _energyState.value = _energyState.value.copy(loading = true)
                val res = energyService.getDailyEnergyConsumption(month = month, day = day)
                _energyState.value = _energyState.value.copy(list = res.data)
            }
            catch (e: Exception){
                _energyState.value = _energyState.value.copy(error = e.toString())
            }
            finally {
                _energyState.value = _energyState.value.copy(loading = false)
            }
        }
    }

    fun getDailyAverageTemperature(){
        viewModelScope.launch {
            try {
                _temperaturesState.value = _temperaturesState.value.copy(loading = true)
                val res = temperatureService.getDailyAverageTemperature(month, day)
                _temperaturesState.value = _temperaturesState.value.copy(list = res.data)
            }
            catch (e: Exception){
                _temperaturesState.value = _temperaturesState.value.copy(error = e.toString())
            }
            finally {
                _temperaturesState.value = _temperaturesState.value.copy(loading = false)
            }
        }
    }

    fun getLatestOutdoorTemperature() {
        viewModelScope.launch {
            try {
                _outdoorTemperatureState.value = _outdoorTemperatureState.value?.copy(loading = true)
                val res = temperatureService.getLatestOutdoorTemperature(month, day)
                _outdoorTemperatureState.value = _outdoorTemperatureState.value?.copy(list = res.data)
            } catch (e: Exception) {
                _outdoorTemperatureState.value = _outdoorTemperatureState.value?.copy(error = e.toString())
            }
            finally {
                _outdoorTemperatureState.value = _outdoorTemperatureState.value?.copy(loading = false)
            }
        }
    }

    fun calculateDate(milSecs: Long?) {
        val date = milSecs?.let { Date(it) }
        val calendar = Calendar.getInstance().apply {
            time = date
        }

        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH) + 1
        day = calendar.get(Calendar.DAY_OF_MONTH)
        setDateString(SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time))

    }
}
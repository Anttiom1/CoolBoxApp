package com.example.androidcoolboxryhma2.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcoolboxryhma2.api.energyService
import com.example.androidcoolboxryhma2.api.temperatureService
import com.example.androidcoolboxryhma2.model.EnergyItem
import com.example.androidcoolboxryhma2.model.EnergyState
import com.example.androidcoolboxryhma2.model.TemperatureItem
import com.example.androidcoolboxryhma2.model.TemperaturesState
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.entry.composed.ComposedChartEntryModelProducer
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
    public val _indoorTemperatureState = mutableStateOf(TemperaturesState())
    val indoorTemperatureState: State<TemperaturesState?> = _indoorTemperatureState
    public val _energyStateWeekly = mutableStateOf(EnergyState())
    val energyStateWeekly: State<EnergyState?> = _energyStateWeekly

    internal val composedChartEntryModelProducer = ComposedChartEntryModelProducer.build()

    val temperatureLineSpec = arrayListOf<LineChart.LineSpec>()
    val electricityLineSpec = arrayListOf<LineChart.LineSpec>()

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

        temperatureLineSpec.add(
            LineChart.LineSpec(
                lineColor = Color.Red.toArgb(),
                lineBackgroundShader = null
            )
        )
        electricityLineSpec.add(
            LineChart.LineSpec(
                lineColor = Color.Blue.toArgb(),
                lineBackgroundShader = null
            )
        )

        initialTime = Instant.now().toEpochMilli()
        calculateDate(initialTime)
        getDailyAverageTemperature()
        getLatestIndoorTemperature()
        getDailyEnergyConsumption()
        getWeeklyEnergyConsumption()
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

    private fun getWeeklyEnergyConsumption(){
        viewModelScope.launch {
            try {
                _energyStateWeekly.value = _energyStateWeekly.value.copy(loading = true)
                val res = energyService.getWeeklyEnergyConsumption(month = month, day = day)
                if (res.data.isNotEmpty()) {
                    val totalConsumedAmount  = energyState.value.list.lastOrNull()?.totalConsumedAmount
                    if (totalConsumedAmount != null) {
                        _energyStateWeekly.value = _energyStateWeekly.value.copy(list = listOf(EnergyItem(year = year, month = month, day = day, hour = 0, totalConsumedAmount = totalConsumedAmount * 7)))
                    }
                } else {
                    val totalConsumedAmount  = energyState.value.list.lastOrNull()?.totalConsumedAmount
                    if (totalConsumedAmount != null) { _energyStateWeekly.value = _energyStateWeekly.value.copy(list = listOf(EnergyItem(year = year, month = month, day = day, hour = 0, totalConsumedAmount = totalConsumedAmount * 7)))
                    }
                }
            }
            catch (e: Exception){
                _energyStateWeekly.value = _energyStateWeekly.value.copy(error = e.toString())
            }
            finally {
                _energyStateWeekly.value = _energyStateWeekly.value.copy(loading = false)
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

    private fun getLatestIndoorTemperature() {
        viewModelScope.launch {
            try {
                _indoorTemperatureState.value = _indoorTemperatureState.value.copy(loading = true)
                val res = temperatureService.getLatestIndoorTemperature(month, day)
                if (res.data.isNotEmpty()) {
                    _indoorTemperatureState.value = _indoorTemperatureState.value.copy(list = listOf(TemperatureItem(deviceName = "sisäasema lämpöanturi", unitName = "Lämpötila", value = "18", unitValue = "°C", year = year, month = month, day = day, hour = 0, minute = 0, sec = 0)))
                } else {
                    _indoorTemperatureState.value = _indoorTemperatureState.value.copy(list = listOf(TemperatureItem(deviceName = "sisäasema lämpöanturi", unitName = "Lämpötila", value = "18", unitValue = "°C", year = year, month = month, day = day, hour = 0, minute = 0, sec = 0)))
                }
            } catch (e: Exception) {
                _indoorTemperatureState.value = _indoorTemperatureState.value.copy(error = e.toString())
            }
            finally {
                _indoorTemperatureState.value = _indoorTemperatureState.value.copy(loading = false)
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
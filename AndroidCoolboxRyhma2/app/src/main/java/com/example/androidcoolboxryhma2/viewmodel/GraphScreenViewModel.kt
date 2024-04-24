package com.example.androidcoolboxryhma2.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcoolboxryhma2.api.energyService
import com.example.androidcoolboxryhma2.api.temperatureService
import com.example.androidcoolboxryhma2.model.EnergyItem
import com.example.androidcoolboxryhma2.model.EnergyState
import com.example.androidcoolboxryhma2.model.TemperaturesState
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
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

    private val _indoorTemperatureState = mutableStateOf(TemperaturesState())
    val indoorTemperatureState: State<TemperaturesState> = _indoorTemperatureState

    private val _energyStateWeekly = mutableStateOf(EnergyState())
    val energyStateWeekly: State<EnergyState> = _energyStateWeekly


    private val _dateString = mutableStateOf("")
    val dateString: State<String> = _dateString

    internal val composedChartEntryModelProducer = ComposedChartEntryModelProducer.build()
    internal val temperatureModelProducer = ChartEntryModelProducer()
    internal val electricityModelProducer = ChartEntryModelProducer()

    internal val temperatureLineSpec = arrayListOf<LineChart.LineSpec>()
    internal val electricityLineSpec = arrayListOf<LineChart.LineSpec>()
    private val datasetForTemperature = mutableStateListOf(listOf<FloatEntry>())
    private val datasetForElectricity = mutableStateListOf(listOf<FloatEntry>())

    private var year: Int = 0
    private var month: Int = 0
    private var week: Int = 0
    private var day: Int = 0
    var initialTime: Long = 0

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
        getLatestIndoorTemperature()

        initialTime = Instant.now().toEpochMilli()
        calculateDate(initialTime)
    }

    private fun getWeeklyEnergyConsumption(){
        viewModelScope.launch {
            try {
                _energyStateWeekly.value = _energyStateWeekly.value.copy(loading = true)
                val res = energyService.getWeeklyEnergyConsumption(week = week)
                _energyStateWeekly.value = _energyStateWeekly.value.copy(list = res.data)
                val total = _energyStateWeekly.value.list.sumByDouble { it.totalConsumedAmount.toDouble() }
                val formattedTotal = String.format("%.3f", total)
                _energyStateWeekly.value = _energyStateWeekly.value.copy(total = formattedTotal)

            }
            catch (e: Exception){
                _energyStateWeekly.value = _energyStateWeekly.value.copy(error = e.toString())
                Log.d("joona", "${_energyStateWeekly.value.error}")
            }
            finally {
                _energyStateWeekly.value = _energyStateWeekly.value.copy(loading = false)
            }
        }
    }

    fun getCombinedData(){
        viewModelScope.launch {
            try {
                _temperaturesState.value = _temperaturesState.value.copy(loading = true)
                val res = temperatureService.getDailyAverageTemperature(month, day)
                _temperaturesState.value = _temperaturesState.value.copy(list = res.data)

                _energyState.value = _energyState.value.copy(loading = true)
                val res2 = energyService.getDailyEnergyConsumption(month = month, day = day)
                _energyState.value = _energyState.value.copy(list = res2.data)
                setDataToCharts()
                val total = _energyState.value.list.sumByDouble { it.totalConsumedAmount.toDouble() }
                val formattedTotal = String.format("%.3f", total)
                _energyState.value = _energyState.value.copy(total = formattedTotal)
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
                val res = temperatureService.getLatestIndoorsTemperature()
                _indoorTemperatureState.value = _indoorTemperatureState.value.copy(list = res.data)
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
        week = calendar.get(Calendar.WEEK_OF_YEAR)
        day = calendar.get(Calendar.DAY_OF_MONTH)
        val formattedDate = (SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time))
        _dateString.value = formattedDate
        getCombinedData()
        getWeeklyEnergyConsumption()

    }

    private fun setDataToCharts(){
        datasetForTemperature.clear()
        datasetForElectricity.clear()
        val dataPoints = arrayListOf<FloatEntry>()
        val dataPoints2 = arrayListOf<FloatEntry>()
        // for loopissa määritellään kaavion pisteet, X = pisteiden määrä Y = lämpötila arvo
        for (item in temperatureState.value.list) {
            val floatValue = item.value.toFloat()
            val xValue = item.hour.toFloat()
            dataPoints.add(FloatEntry(x = xValue, y = floatValue))
        }

        for (item in energyState.value.list) {
            val floatValue = item.totalConsumedAmount
            val xValue = item.hour.toFloat()
            dataPoints2.add(FloatEntry(x = xValue, y = floatValue))
        }
        datasetForTemperature.add(dataPoints)
        datasetForElectricity.add(dataPoints2)
        temperatureModelProducer.setEntries(datasetForTemperature)
        electricityModelProducer.setEntries(datasetForElectricity)
        composedChartEntryModelProducer.runTransaction {
            add(datasetForTemperature)
            add(datasetForElectricity)
            }

    }
}
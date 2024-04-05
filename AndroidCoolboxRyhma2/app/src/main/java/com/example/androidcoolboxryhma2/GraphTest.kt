package com.example.androidcoolboxryhma2


import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidcoolboxryhma2.viewmodel.GraphTestViewModel
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.compose.component.marker.markerComponent
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.DefaultAlpha
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.chart.Chart
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.component.Component
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShader
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.component.text.TextComponent
import com.patrykandpatrick.vico.core.component.text.textComponent
import com.patrykandpatrick.vico.core.dimensions.MutableDimensions
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.extension.getFieldValue
import com.patrykandpatrick.vico.core.extension.round
import com.patrykandpatrick.vico.core.marker.Marker
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent
import java.util.Date
import kotlin.math.pow
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraphTest(goToHome: () -> Unit) {
    val vm: GraphTestViewModel = viewModel()

    val modelProducer = remember { ChartEntryModelProducer() }
    val datasetForModel = remember { mutableStateListOf(listOf<FloatEntry>()) }
    val datasetLineSpec = remember { arrayListOf<LineChart.LineSpec>() }
    val scrollState = rememberChartScrollState()
    val openDialog = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(vm.initialTime)


    fun decreaseDay(){
        val newTime = datePickerState.selectedDateMillis?.minus(
            86400000
        )
        datePickerState.selectedDateMillis = newTime
        vm.calculateDate(datePickerState.selectedDateMillis)
        vm.getDailyAverageTemperature()
    }

    fun incrementDay(){
        val newTime = datePickerState.selectedDateMillis?.plus(
            86400000
        )
        datePickerState.selectedDateMillis = newTime
        vm.calculateDate(datePickerState.selectedDateMillis)
        vm.getDailyAverageTemperature()
    }

    // LaunchedEffect aktivoituu aina kun lista muuttuu
    LaunchedEffect(key1 = vm.temperatureState.value.list) {
        datasetForModel.clear()
        datasetLineSpec.clear()
        val dataPoints = arrayListOf<FloatEntry>()
        datasetLineSpec.add(
            LineChart.LineSpec(
                lineColor = Red.toArgb(),
                lineBackgroundShader = DynamicShaders.fromBrush(
                    brush = Brush.verticalGradient(
                        listOf(
                            Red.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_START),
                            Red.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_END)
                        )
                    )
                )
            )
        )
        // for loopissa määritellään kaavion pisteet, X = pisteiden määrä Y = lämpötila arvo
        for (item in vm.temperatureState.value.list) {
            val floatValue = item.value.toFloat()
            val xValue = item.hour.toFloat()
            Log.d("mursu", xValue.toString())
            dataPoints.add(FloatEntry(x = xValue, y = floatValue))
        }

        if (dataPoints.isNotEmpty()) {
            datasetForModel.add(dataPoints)
            modelProducer.setEntries(datasetForModel)
        }
    }


    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold (
            topBar = {
                TopAppBar (
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                        title = {
                    Text(text = "Graph")
                },
                    actions = {
                        IconButton(onClick = {goToHome()}) {
                            Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
                        }
                    })
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(8.dp)
                    .fillMaxSize()
            ) {
                OutlinedTextField(value = vm.getDateString(),
                    onValueChange = { },
                    enabled = true,
                    leadingIcon = {
                        IconButton(onClick = { openDialog.value = true }) {
                            Icon(imageVector = Icons.Default.DateRange, contentDescription = "Date")
                        }
                    },
                    trailingIcon = {
                        Row {
                            IconButton(onClick = { decreaseDay() }) {
                                Icon(
                                    Icons.Default.KeyboardArrowLeft,
                                    contentDescription = "Previous"
                                )
                            }
                            IconButton(onClick = { incrementDay() }) {
                                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next")
                            }
                        }
                    }

                )
                Spacer(modifier = Modifier.height(8.dp))

                if (openDialog.value) {
                    val confirmEnabled = remember {
                        derivedStateOf { datePickerState.selectedDateMillis != null }
                    }
                    DatePickerDialog(
                        onDismissRequest = {
                            openDialog.value = false
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    openDialog.value = false
                                    vm.calculateDate(datePickerState.selectedDateMillis)
                                    vm.getDailyAverageTemperature()
                                },
                                enabled = confirmEnabled.value
                            ) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    openDialog.value = false
                                }
                            ) {
                                Text("Cancel")
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }
                Card(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .height(440.dp)
                ) {
                    if (datasetForModel.isNotEmpty()) {
                        ProvideChartStyle {
                            val marker = rememberMarker()
                            Text(text = "Ulkolämpötila", fontSize = 24.sp, modifier = Modifier.padding(8.dp))
                            Chart(
                                modifier = Modifier.fillMaxSize(),
                                marker = marker,
                                chart = lineChart(
                                    axisValuesOverrider = AxisValuesOverrider.fixed(minY = -25f, maxY = 25f),
                                    lines = datasetLineSpec
                                ),
                                chartModelProducer = modelProducer,

                                startAxis = rememberStartAxis(
                                    title = "Top values",
                                    tickLength = 0.dp,
                                    valueFormatter = { value, _ ->
                                        value.round.toString()
                                    },
                                    itemPlacer = AxisItemPlacer.Vertical.default(13)
                                ),

                                bottomAxis = rememberBottomAxis(
                                    title = "Count of values",
                                    tickLength = 0.dp,
                                    valueFormatter = { value, _ ->
                                        value.toString()
                                    },
                                    guideline = null
                                ),

                                chartScrollState = scrollState,
                                isZoomEnabled = true
                            )
                        }
                    }

                }
            }
        }
    }
}
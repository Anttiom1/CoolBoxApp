package com.example.androidcoolboxryhma2


import android.text.Layout
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.size.Dimension
import com.example.androidcoolboxryhma2.viewmodel.GraphScreenViewModel
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.core.DefaultAlpha
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.chart.column.ColumnChart
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.component.shape.DashedShape
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShader
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.component.text.TextComponent
import com.patrykandpatrick.vico.core.dimensions.MutableDimensions
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.extension.mutableListOf
import com.patrykandpatrick.vico.core.extension.round
import com.patrykandpatrick.vico.core.marker.Marker
import kotlin.math.round
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraphScreen(goToHome: () -> Unit,
                onMenuClick: () -> Unit,
                juuhOnClick: (index: Int) -> Unit = {}) {
    val vm: GraphScreenViewModel = viewModel()

    val modelProducer = remember { ChartEntryModelProducer() }
    val columnModelProducer = remember { ChartEntryModelProducer() }
    val datasetForModel = remember { mutableStateListOf(listOf<FloatEntry>()) }
    val datasetLineSpec = remember { arrayListOf<LineChart.LineSpec>() }
    val columnLineSpec = remember { arrayListOf<LineComponent>() }
    val scrollState = rememberChartScrollState()
    val openDialog = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(vm.initialTime)
    var expanded by remember { mutableStateOf(false)}
    val menuOptions = arrayOf("Ulkolämpötila", "Sähkönkulutus")
    var selectedOption by remember { mutableStateOf(menuOptions[0])}

    fun decreaseDay(){
        val newTime = datePickerState.selectedDateMillis?.minus(
            86400000
        )
        datePickerState.selectedDateMillis = newTime
        vm.calculateDate(datePickerState.selectedDateMillis)
        if (selectedOption == "Ulkolämpötila"){
            vm.getDailyAverageTemperature()
        }
        if (selectedOption == "Sähkönkulutus"){
            vm.getDailyEnergyConsumption()
        }
    }

    fun incrementDay(){
        val newTime = datePickerState.selectedDateMillis?.plus(
            86400000
        )
        datePickerState.selectedDateMillis = newTime
        vm.calculateDate(datePickerState.selectedDateMillis)
        if (selectedOption == "Ulkolämpötila"){
            vm.getDailyAverageTemperature()
        }
        if (selectedOption == "Sähkönkulutus"){
            vm.getDailyEnergyConsumption()
        }
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
            dataPoints.add(FloatEntry(x = xValue, y = floatValue))
        }

        if (dataPoints.isNotEmpty()) {
            datasetForModel.add(dataPoints)
            modelProducer.setEntries(datasetForModel)
        }
    }

    LaunchedEffect(key1 = vm.energyState.value.list) {

        val dataPoints = arrayListOf<FloatEntry>()
        datasetForModel.clear()
        datasetLineSpec.clear()
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
        for (item in vm.energyState.value.list) {
            val floatValue = item.totalConsumedAmount
            val xValue = item.hour.toFloat()
            dataPoints.add(FloatEntry(x = xValue, y = floatValue))
        }

        if (dataPoints.isNotEmpty()) {
            datasetForModel.add(dataPoints)
            columnModelProducer.setEntries(datasetForModel)
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { onMenuClick() }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    title = {
                        Text(text = "Graph")
                    },
                    actions = {
                        IconButton(onClick = { goToHome() }) {
                            Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
                        }
                    })
            }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Column {
                    OutlinedTextField(value = vm.getDateString(),
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        onValueChange = { },
                        readOnly = true,
                        leadingIcon = {
                            IconButton(onClick = { openDialog.value = true }) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Date"
                                )
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
                                    Icon(
                                        Icons.Default.KeyboardArrowRight,
                                        contentDescription = "Next"
                                    )
                                }
                            }
                        }
                    )
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
                                        if (selectedOption == "Ulkolämpötila") {
                                            vm.getDailyAverageTemperature()
                                        }
                                        if (selectedOption == "Sähkönkulutus") {
                                            vm.getDailyEnergyConsumption()
                                        }

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
                    val cornerRadius = 8.dp

                    val (selectedIndex, onIndexSelected) = remember { mutableStateOf<Int?>(null) }
                    Row(
                        modifier =
                        Modifier.padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        menuOptions.forEachIndexed { index, item ->
                            OutlinedButton(
                                modifier = when (index) {
                                    0 ->
                                        Modifier
                                            .offset(0.dp, 0.dp)
                                            .zIndex(if (selectedIndex == index) 1f else 0f)

                                    else ->
                                        Modifier
                                            .offset((-1 * index).dp, 0.dp)
                                            .zIndex(if (selectedIndex == index) 1f else 0f)
                                }
                                    .weight(1F).fillMaxWidth(),
                                onClick = {
                                    onIndexSelected(index)
                                    juuhOnClick(index)
                                    selectedOption = item
                                    if (selectedOption == "Sähkönkulutus") {
                                        vm.getDailyEnergyConsumption()
                                    }
                                    if (selectedOption == "Ulkolämpötila") {
                                        vm.getDailyAverageTemperature()
                                    }
                                },
                                shape = when (index) {
                                    // left outer button
                                    0 -> RoundedCornerShape(
                                        topStart = cornerRadius,
                                        topEnd = 0.dp,
                                        bottomStart = cornerRadius,
                                        bottomEnd = 0.dp
                                    )
                                    // right outer button
                                    menuOptions.size - 1 -> RoundedCornerShape(
                                        topStart = 0.dp,
                                        topEnd = cornerRadius,
                                        bottomStart = 0.dp,
                                        bottomEnd = cornerRadius
                                    )
                                    // middle button
                                    else -> RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 0.dp, bottomEnd = 0.dp)
                                },
                                border = BorderStroke(
                                    1.dp, if (selectedIndex == index) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        Color.DarkGray.copy(alpha = 0.75f)
                                    }
                                ),
                                colors = if (selectedIndex == index) {
                                    // selected colors
                                    ButtonDefaults.outlinedButtonColors(
                                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                        contentColor = MaterialTheme.colorScheme.primary
                                    )
                                } else {
                                    // not selected colors
                                    ButtonDefaults.outlinedButtonColors(
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        contentColor = MaterialTheme.colorScheme.primary
                                    )
                                },
                            ) {
                                Text(
                                    text = item,
                                    color = if (selectedIndex == index) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        Color.DarkGray.copy(alpha = 0.9f)
                                    },
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                            }
                        }
                    }
                    if (selectedOption == "Sähkönkulutus") {
                        val marker = rememberMarker()
                        Chart(
                            marker = marker,
                            startAxis = rememberStartAxis(
                                itemPlacer = AxisItemPlacer.Vertical.default(13),
                                valueFormatter = { value, _ ->
                                    String.format("%.2f", value) + "kWh"
                                }
                            ),
                            bottomAxis = rememberBottomAxis(
                                valueFormatter = { value, _ ->
                                    value.toInt().toString()
                                }
                            ),
                            chartModelProducer = columnModelProducer,
                            modifier = Modifier.fillMaxSize(),
                            chart = lineChart(
                                axisValuesOverrider = AxisValuesOverrider.fixed(
                                    minY = 0f,
                                    maxY = 1f
                                ),
                                lines = datasetLineSpec,

                                )
                        )
                    }
                    if (selectedOption == "Ulkolämpötila") {
                        val marker = rememberMarker()
                        val label = TextComponent.Builder()

                        Chart(
                            modifier = Modifier.fillMaxSize(),
                            marker = marker,
                            chart = lineChart(
                                axisValuesOverrider = AxisValuesOverrider.fixed(
                                    minY = -25f,
                                    maxY = 25f
                                ),
                                lines = datasetLineSpec
                            ),
                            chartModelProducer = modelProducer,

                            startAxis = rememberStartAxis(
                                valueFormatter = { value, _ ->
                                    value.round.toString() + "°C"
                                },
                                itemPlacer = AxisItemPlacer.Vertical.default(13)
                            ),

                            bottomAxis = rememberBottomAxis(
                                label = label.build(),
                                valueFormatter = { value, _ ->
                                    value.toInt().toString()
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


package com.example.androidcoolboxryhma2


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidcoolboxryhma2.viewmodel.GraphTestViewModel
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.DefaultAlpha
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.chart.Chart
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShader
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.extension.round
import java.nio.file.WatchEvent
import kotlin.math.pow
import kotlin.math.round


@Composable
fun GraphTest(onLoginClick: () -> Unit){
    val vm: GraphTestViewModel = viewModel()

    val refreshDataset = remember { mutableIntStateOf(0) }
    val modelProducer = remember { ChartEntryModelProducer() }
    val datasetForModel = remember { mutableStateListOf(listOf<FloatEntry>()) }
    val datasetLineSpec = remember { arrayListOf<LineChart.LineSpec>() }
    val scrollState = rememberChartScrollState()


    // LaunchedEffect aktivoituu aina kun lista muuttuu
    LaunchedEffect(key1 = vm.temperatureState.value.list){
        datasetForModel.clear()
        datasetLineSpec.clear()
        var xPos = 0f
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
        for(item in vm.temperatureState.value.list){
            val floatValue = item.value.toFloat()
            dataPoints.add(FloatEntry(x = xPos, y = floatValue))
            xPos += 1f
        }

        if(dataPoints.isNotEmpty()){
            datasetForModel.add(dataPoints)
            modelProducer.setEntries(datasetForModel)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Card(modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
        ) {
            if (datasetForModel.isNotEmpty()){
                ProvideChartStyle {
                    Chart(
                        chart = lineChart(
                            lines = datasetLineSpec
                        ),
                        chartModelProducer = modelProducer,

                        startAxis = rememberStartAxis(
                            title = "Top values",
                            tickLength = 0.dp,
                            valueFormatter = {value, _ ->
                                 round(value, 1).toString()
                            },
                            itemPlacer = AxisItemPlacer.Vertical.default(
                                maxItemCount = 6
                            )
                        ),

                        bottomAxis = rememberBottomAxis(
                            title = "Count of values",
                            tickLength = 0.dp,
                            valueFormatter = {value, _ ->
                                ((value.toInt()) + 1).toString()
                            },
                            guideline = null
                        ),

                        chartScrollState = scrollState,
                        isZoomEnabled = true
                    )
                }
            }

        }
        /*TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {refreshDataset.intValue++}
        ) {
            Text(text = "Refresh")

        }*/
        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { vm.getLatestTemperatureIndoors() }
        ) {
            Text(text = "Get temperature")

        }
    }
}

fun round(value: Float, decimalPlaces: Int): Float {
    val factor = 10.0.pow(decimalPlaces)
    return (kotlin.math.round(value * factor) / factor).toFloat()
}
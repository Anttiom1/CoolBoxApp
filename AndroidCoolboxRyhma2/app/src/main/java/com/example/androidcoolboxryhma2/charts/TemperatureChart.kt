package com.example.androidcoolboxryhma2.charts

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.androidcoolboxryhma2.rememberMarker
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.ChartScrollState
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.component.text.TextComponent
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.extension.round

@Composable
fun TemperatureChart(modelProducer: ChartEntryModelProducer, scrollState: ChartScrollState, datasetLineSpec: List<LineChart.LineSpec> ){
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
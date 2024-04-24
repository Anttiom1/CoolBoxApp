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
fun ElectricityChart(modelProducer: ChartEntryModelProducer, scrollState: ChartScrollState, datasetLineSpec: List<LineChart.LineSpec>){
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
            },
            guideline = null
        ),
        chartModelProducer = modelProducer,
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
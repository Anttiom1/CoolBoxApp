package com.example.androidcoolboxryhma2.charts

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.androidcoolboxryhma2.rememberMarker
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberEndAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.chart.composed.plus
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.entry.composed.ComposedChartEntryModelProducer
import com.patrykandpatrick.vico.core.extension.round

@Composable
fun CombinedChart(temperatureLineSpec: List<LineChart.LineSpec>, electricityLineSpec: List<LineChart.LineSpec>, modelProducer: ComposedChartEntryModelProducer){
    val chart1 = lineChart(axisValuesOverrider = AxisValuesOverrider.fixed(
        minY = -25f,
        maxY = 25f
    ),
        targetVerticalAxisPosition = AxisPosition.Vertical.Start,
        lines = temperatureLineSpec)
    val chart2 = lineChart(axisValuesOverrider = AxisValuesOverrider.fixed(
        minY = 0f,
        maxY = 1f
    ),
        targetVerticalAxisPosition = AxisPosition.Vertical.End,
        lines = electricityLineSpec,)
    Chart(
        modifier = Modifier.fillMaxSize(),
        chart = remember(chart1, chart2) { chart1 + chart2 },
        chartModelProducer = modelProducer,
        startAxis = rememberStartAxis(
            guideline = null,
            valueFormatter = { value, _ ->
                value.round.toString() + "°C"
            },
            itemPlacer = AxisItemPlacer.Vertical.default(13)),
        endAxis = rememberEndAxis(
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
        marker = rememberMarker(),
    )
}
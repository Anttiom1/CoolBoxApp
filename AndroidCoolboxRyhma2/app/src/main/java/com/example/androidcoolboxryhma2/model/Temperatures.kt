package com.example.androidcoolboxryhma2.model

import com.google.gson.annotations.SerializedName

data class TemperaturesState(
    val list: List<TemperatureItem> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)

data class TemperatureItem(
    @SerializedName("device_name")
    val deviceName: String,
    @SerializedName("unit_name")
    val unitName: String,
    @SerializedName("value")
    val value: String,
    @SerializedName("unit_value")
    val unitValue: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    val sec: Int
)

data class TemperatureData(
    val data: List<TemperatureItem>
)


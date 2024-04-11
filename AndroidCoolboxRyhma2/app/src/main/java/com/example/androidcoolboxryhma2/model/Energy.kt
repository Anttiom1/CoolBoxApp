package com.example.androidcoolboxryhma2.model

import com.google.gson.annotations.SerializedName

data class EnergyState(
    val list: List<EnergyItem> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)
data class EnergyItem(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    @SerializedName("total_consumed_amount")
    val totalConsumedAmount: Float
)
data class EnergyData(
    val data: List<EnergyItem>
)
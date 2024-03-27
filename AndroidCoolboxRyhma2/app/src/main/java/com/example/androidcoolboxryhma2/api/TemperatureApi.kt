package com.example.androidcoolboxryhma2.api

import com.example.androidcoolboxryhma2.model.TemperatureData
import com.example.androidcoolboxryhma2.model.TemperatureItem

import retrofit2.http.GET

private val retrofit = createClient()

val temperatureService = retrofit.create(TemperatureApi::class.java)

interface TemperatureApi{
    @GET("temperature/indoors/latest")
    suspend fun getLatestTemperatureIndoors(): TemperatureData
}
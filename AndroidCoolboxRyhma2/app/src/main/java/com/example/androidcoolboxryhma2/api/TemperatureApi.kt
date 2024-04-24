package com.example.androidcoolboxryhma2.api

import com.example.androidcoolboxryhma2.model.TemperatureData
import retrofit2.http.GET
import retrofit2.http.Path

private val retrofit = createClient()

val temperatureService = retrofit.create(TemperatureApi::class.java)

interface TemperatureApi{

    @GET("temperature/{month}/{day}")
    suspend fun getDailyAverageTemperature(@Path("month") month: Int, @Path("day") day: Int): TemperatureData

    @GET("temperature/latest")
    suspend fun getLatestOutdoorsTemperature(): TemperatureData

    @GET("temperature/indoors/latest")
    suspend fun getLatestIndoorsTemperature(): TemperatureData
}
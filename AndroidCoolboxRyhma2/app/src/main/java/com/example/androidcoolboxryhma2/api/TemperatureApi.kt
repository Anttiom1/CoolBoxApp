package com.example.androidcoolboxryhma2.api

import com.example.androidcoolboxryhma2.model.TemperatureData
import com.example.androidcoolboxryhma2.model.TemperatureItem

import retrofit2.http.GET
import retrofit2.http.Path

private val retrofit = createClient()

val temperatureService = retrofit.create(TemperatureApi::class.java)

interface TemperatureApi{
    @GET("temperature/{week}")
    suspend fun getLatestTemperatureTest(@Path("week") week: Int): TemperatureData

    @GET("temperature/{month}/{day}")
    suspend fun getDailyAverageTemperature(@Path("month") month: Int, @Path("day") day: Int): TemperatureData
}
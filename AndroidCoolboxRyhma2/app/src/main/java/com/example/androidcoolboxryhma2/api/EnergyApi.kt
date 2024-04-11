package com.example.androidcoolboxryhma2.api

import com.example.androidcoolboxryhma2.model.EnergyData
import retrofit2.http.GET
import retrofit2.http.Path


private val retrofit = createClient()

val energyService = retrofit.create(EnergyApi::class.java)

interface EnergyApi{
    @GET("energy/{month}/{day}")
    suspend fun getDailyEnergyConsumption(@Path("month") month: Int, @Path("day") day: Int): EnergyData

}


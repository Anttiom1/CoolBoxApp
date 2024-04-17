package com.example.androidcoolboxryhma2.api

import com.example.androidcoolboxryhma2.model.AuthReq
import com.example.androidcoolboxryhma2.model.AuthRes
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

private val retrofitClient = Retrofit.Builder().baseUrl("http://10.0.2.2:8000/api/v1/")
    .addConverterFactory(GsonConverterFactory.create()).build()


val authService = retrofitClient.create(AuthApi::class.java)

interface AuthApi {
    @POST("/api/login")
    suspend fun login(@Body req: AuthReq) : AuthRes

    @POST("/api/logout")
    suspend fun logout(@Header("Authorization") bearerToken: String)
}
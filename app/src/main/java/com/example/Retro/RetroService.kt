package com.example.Retro

import com.example.data.LoginRequestData
import com.example.data.LoginResponseData
import com.example.data.RegisterRequestData
import com.example.data.RegisterResponseData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface RetroService {

    @POST("user/login/")
    @Headers("Content-Type: application/json")
    fun loginUser(@Body params: LoginRequestData): Call<LoginResponseData>

    @POST("user/register/")
    @Headers("Content-Type: application/json")
    fun registerUser(@Body params: RegisterRequestData): Call<RegisterResponseData>
}
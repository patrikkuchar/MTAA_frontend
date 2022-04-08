package com.example

import com.example.data.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.DELETE

interface RestApiInterface {

    @Headers("Content-Type: application/json")
    @POST("user/login/")
    fun loginUser(@Body userData: LoginRequestData): Call<LoginResponseData>
}
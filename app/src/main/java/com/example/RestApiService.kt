package com.example

import com.example.data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class RestApiService {
    fun loginUser(userData: LoginRequestData, onResult: (LoginResponseData?) -> Unit){
        val retrofit = RetrofitBuilder.buildService(RestApiInterface::class.java)
        retrofit.loginUser(userData).enqueue(
            object : Callback<LoginResponseData> {
                override fun onFailure(call: Call<LoginResponseData>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse( call: Call<LoginResponseData>, response: Response<LoginResponseData>) {
                    val loginUser = response.body()
                    onResult(loginUser)
                }
            }
        )
    }
}
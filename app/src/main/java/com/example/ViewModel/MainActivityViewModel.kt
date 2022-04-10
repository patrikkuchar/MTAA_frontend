package com.demo.retrofithttpmethods

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Retro.RetroInstance
import com.example.Retro.RetroService
import com.example.data.LoginRequestData
import com.example.data.LoginResponseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response






class MainActivityViewModel: ViewModel() {

    lateinit var login_user_response: MutableLiveData<LoginResponseData?>
    init {
        login_user_response = MutableLiveData()
    }

    fun login_user(user: LoginRequestData) {
        var retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
        val call = retroInstance.loginUser(user)
        call.enqueue(object: Callback<LoginResponseData> {
            override fun onFailure(call: Call<LoginResponseData>, t: Throwable) {
                println("SDADADSA")
                login_user_response.postValue(null)
                println("SSSSSS")
            }

            override fun onResponse(call: Call<LoginResponseData>, response: Response<LoginResponseData>) {
                if(response.isSuccessful) {
                    login_user_response.postValue(response.body())


                } else {
                    login_user_response.postValue(null)
                    println("HEYY")

                }
            }
        })
    }


}
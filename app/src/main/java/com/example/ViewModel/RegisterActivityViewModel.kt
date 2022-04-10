package com.example.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Retro.RetroInstance
import com.example.Retro.RetroService
import com.example.data.LoginResponseData
import com.example.data.RegisterRequestData
import com.example.data.RegisterResponseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivityViewModel: ViewModel() {
    lateinit var register_user_response: MutableLiveData<RegisterResponseData?>
    init {
        register_user_response = MutableLiveData()
    }






    fun register_user(user: RegisterRequestData){
        var retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
        val call = retroInstance.registerUser(user)
        call.enqueue(object: Callback<RegisterResponseData> {
            override fun onFailure(call: Call<RegisterResponseData>, t: Throwable) {

                register_user_response.postValue(null)
            }

            override fun onResponse(call: Call<RegisterResponseData>, response: Response<RegisterResponseData>) {
                if(response.isSuccessful) {
                    val response_code = RegisterResponseData(response.code())
                    register_user_response.postValue(response_code)
                } else {
                    val response_code = RegisterResponseData(response.code())
                    register_user_response.postValue(response_code)

                }
            }
        })
    }



}
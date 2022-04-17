package com.example.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Retro.RetroInstance
import com.example.Retro.RetroService
import com.example.data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ProfileActivityViewModel: ViewModel() {
    lateinit var properties: MutableLiveData<UserProperties>
    lateinit var response_delete: MutableLiveData<Int>
    init {
        properties = MutableLiveData()
        response_delete = MutableLiveData()
    }


    fun delete_property(token:String,property_id: Int) {
        var retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
        val call = retroInstance.delete_property(property_id,token)
        call.enqueue(object: Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                println("fail1")
            }
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    println("success")
                }

                else {
                    println("fail2")
                }

            }
        })
    }

    fun getUserProperties(token:String) {
        var retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
        val call = retroInstance.get_user_property(token)
        call.enqueue(object: Callback<UserProperties> {
            override fun onFailure(call: Call<UserProperties>, t: Throwable) {
                println("fail1")
                properties.postValue(null)
            }
            override fun onResponse(call: Call<UserProperties>, response: Response<UserProperties>) {
                if(response.isSuccessful) {
                    properties.postValue(response.body())
                    println("success")
                }

                else {
                    properties.postValue(null)
                    println("fail2")
                }

            }
        })
    }



    fun change_password(token:String,request: Edit_Password_Request){
        var retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
        val call = retroInstance.edit_password(request,token)
        call.enqueue(object: Callback<ChangePasswordResponse> {
            override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                println("fail1")
            }
            override fun onResponse(call: Call<ChangePasswordResponse>, response: Response<ChangePasswordResponse>) {
                if(response.isSuccessful) {
                    println("success")
                }

                else {
                    println("fail2")
                }

            }
        })
    }
}
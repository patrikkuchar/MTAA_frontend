package com.example.ViewModel

import androidx.lifecycle.ViewModel
import com.example.Retro.RetroInstance
import com.example.Retro.RetroService
import com.example.data.ChangePasswordResponse
import com.example.data.Edit_Password_Request
import com.example.data.PropertyInfoDataOne
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivityViewModel: ViewModel() {



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
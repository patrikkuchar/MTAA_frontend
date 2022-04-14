package com.example.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Retro.RetroInstance
import com.example.Retro.RetroService
import com.example.data.ChangePasswordResponse
import com.example.data.Edit_Password_Request
import com.example.data.Propety_list
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivityViewModel: ViewModel() {
    lateinit var response_pass: MutableLiveData<Int>
    init {
        response_pass = MutableLiveData()
    }


    fun change_password(token:String,request: Edit_Password_Request){
        var retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
        val call = retroInstance.edit_password(request,token)
        println(request)
        call.enqueue(object: Callback<ChangePasswordResponse> {
            override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                response_pass.postValue(null)
            }
            override fun onResponse(call: Call<ChangePasswordResponse>, response: Response<ChangePasswordResponse>) {
                if(response.isSuccessful) {
                    response_pass.postValue(response.code())
                }

                else {
                    response_pass.postValue(response.code())

                }

            }
        })
    }
}
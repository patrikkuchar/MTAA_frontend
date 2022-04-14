package com.example.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Retro.RetroInstance
import com.example.Retro.RetroService
import com.example.data.PropertyInfoData
import com.example.data.PropertyInfoDataOne
import com.example.data.Propety_list
import com.example.data.Subregion_list
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PropertyInfoViewModel: ViewModel() {
    lateinit var property: MutableLiveData<PropertyInfoData>
    init{
        property = MutableLiveData()
    }

    fun get_property_info(token:String,propertyId:Int){
        var retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
        val call = retroInstance.get_property(propertyId,token)
        call.enqueue(object: Callback<PropertyInfoDataOne> {
            override fun onFailure(call: Call<PropertyInfoDataOne>, t: Throwable) {
                println("fail1")
            }
            override fun onResponse(call: Call<PropertyInfoDataOne>, response: Response<PropertyInfoDataOne>) {
                if(response.isSuccessful) {
                    property.postValue(response.body()?.property)

                } else {
                    println("fail2")


                }

            }
        })
    }
}
package com.example.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Retro.RetroInstance
import com.example.Retro.RetroService
import com.example.data.PropertyInfoData
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
        call.enqueue(object: Callback<PropertyInfoData> {
            override fun onFailure(call: Call<PropertyInfoData>, t: Throwable) {
                println("fail1")
            }
            override fun onResponse(call: Call<PropertyInfoData>, response: Response<PropertyInfoData>) {
                if(response.isSuccessful) {
                    property.postValue(response.body())

                } else {
                    println("fail2")


                }

            }
        })
    }
}
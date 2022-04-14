package com.example.ViewModel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Retro.RetroInstance
import com.example.Retro.RetroService
import com.example.data.*
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

    fun add_booking(token:String,propertyId: Int,startDate:String) {
        var retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
        val request = AddBookingData(propertyId,startDate)
        val call = retroInstance.add_booking(request,token)
        call.enqueue(object: Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                println("fail1")
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    if (response.code().toInt() == 201) {
                        println("success")
                        Toast.makeText(null, "Booking Successful Added", Toast.LENGTH_SHORT).show()
                    } else {
                        println("fail2")
                    }
                } else {
                    println("fail3")
                }
            }
        })

    }
}
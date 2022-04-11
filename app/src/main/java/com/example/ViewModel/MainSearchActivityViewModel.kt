package com.example.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Retro.RetroInstance
import com.example.Retro.RetroService
import com.example.data.*
import com.example.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainSearchActivityViewModel: ViewModel() {
    lateinit var properties: MutableLiveData<Propety_list>
    lateinit var regions:MutableLiveData<Region_List>
    lateinit var subregions:MutableLiveData<Subregion_list>
    init {
        properties = MutableLiveData()
        regions = MutableLiveData()
        subregions = MutableLiveData()
    }

    fun filter(token:String , region_id:String , subregion_id:String , price_min_max:String,area_min_max:String,rooms:String){
        var retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)

        val call = retroInstance.filter(region_id,subregion_id,price_min_max,area_min_max,rooms,token)
        call.enqueue(object: Callback<Propety_list>{
            override fun onFailure(call: Call<Propety_list>, t: Throwable) {
                println("SDADADSA")
                properties.postValue(null)
                println("SSSSSS")
            }

            override fun onResponse(call: Call<Propety_list>, response: Response<Propety_list>) {
                if(response.isSuccessful) {
                    properties.postValue(response.body())

                } else {
                    properties.postValue(null)
                    println("HEYY")

                }
            }
        })
    }


    fun get_subregions(token:String,regionId:Int){
        var retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
        val call = retroInstance.get_subregions(regionId,token)
        call.enqueue(object: Callback<Subregion_list>{
            override fun onFailure(call: Call<Subregion_list>, t: Throwable) {
                subregions.postValue(null)

            }

            override fun onResponse(call: Call<Subregion_list>, response: Response<Subregion_list>) {
                if(response.isSuccessful) {
                    subregions.postValue(response.body())
                } else {
                    subregions.postValue(null)

                }
            }
        })
    }

    fun get_regions(token:String){
        var retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
        val call = retroInstance.get_regions(token)
        call.enqueue(object: Callback<Region_List>{
            override fun onFailure(call: Call<Region_List>, t: Throwable) {
                regions.postValue(null)

            }

            override fun onResponse(call: Call<Region_List>, response: Response<Region_List>) {
                if(response.isSuccessful) {
                    regions.postValue(response.body())
                } else {
                    regions.postValue(null)

                }
            }
        })
    }

}
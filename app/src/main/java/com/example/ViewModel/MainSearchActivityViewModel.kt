package com.example.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Activity.MainSearchActivity
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
    lateinit var liked:MutableLiveData<Propety_list>
    init {
        properties = MutableLiveData()
        regions = MutableLiveData()
        subregions = MutableLiveData()
        liked = MutableLiveData()
    }

    fun likedProperties(property_id:Int,context: MainSearchActivity){
        var retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
        var token = SharedPrefManager.getInstance(context).user.token.toString()
        token = "Bearer $token"
        val request = Add_Liked_Request(property_id)

        val call = retroInstance.add_like(request,token)

        call.enqueue(object: Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                println("SDADADSA")
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    if (response.code().toInt() == 200) {
                        println("MARIJAI SDA SOM TU UUUUTUTUTUTUTUTUTUTUT")
                        println(response.body())
                    }

                } else {

                }
            }
        })
    }


    fun unlikedProperties(property_id:Int,context: MainSearchActivity){
        var retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
        var token = SharedPrefManager.getInstance(context).user.token.toString()
        token = "Bearer $token"

        val call = retroInstance.delete_like(property_id,token)

        call.enqueue(object: Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                println("SDADADSA")
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {

                    println("MARIJAI SDA SOM TU UUUUTUTUTUTUTUTUTUTUT")
                    println(response.body())

                } else {

                }
            }
        })
    }

    fun get_liked(token: String){
        var retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
        val call = retroInstance.get_liked(token)

        call.enqueue(object: Callback<Propety_list>{
            override fun onFailure(call: Call<Propety_list>, t: Throwable) {
                println("SDADADSA")
            }

            override fun onResponse(call: Call<Propety_list>, response: Response<Propety_list>) {
                if(response.isSuccessful) {
                    if (response.code().toInt() == 200) {
                        liked.postValue(response.body())
                    }
                } else
                {
                    liked.postValue(null)
                    print("response_not_succesful")
                }
            }
        })
    }


    fun filter(token:String , region_id:String , subregion_id:String , price_min_max:String,area_min_max:String,rooms:String,likedList: Propety_list?){
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

                    //iterate through properties
                    if (likedList != null) {
                        if(likedList.properties != null){
                            for(i in 0 until response.body()!!.properties.size){
                                for(j in 0 until likedList!!.properties.size){
                                    if(response.body()!!.properties[i].id == likedList!!.properties[j].id){
                                        response.body()!!.properties[i].liked = true
                                    }
                                }
                            }
                        }
                    }
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
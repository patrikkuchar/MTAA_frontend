package com.example.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Retro.RetroInstance
import com.example.Retro.RetroService
import com.example.data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SellPropertyActivityViewModel: ViewModel() {
    lateinit var response_code: MutableLiveData<String>
    lateinit var regions: MutableLiveData<Region_List>
    lateinit var subregions:MutableLiveData<Subregion_list>
    init {
        response_code = MutableLiveData()
        regions = MutableLiveData()
        subregions = MutableLiveData()
    }



    fun sell_property(token: String,PropertyRequest: SellPropertyRequest){
        var retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
        val call = retroInstance.add_property(PropertyRequest,token)
        call.enqueue(object: Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {

                response_code.postValue(null)
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    response_code.postValue(response.code().toString())
                } else {
                    response_code.postValue(response.code().toString())

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


}
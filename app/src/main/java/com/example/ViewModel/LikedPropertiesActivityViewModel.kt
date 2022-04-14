package com.example.ViewModel

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Retro.RetroInstance
import com.example.Retro.RetroService
import com.example.data.Propety_list
import com.example.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LikedPropertiesActivityViewModel: ViewModel() {
    lateinit var liked: MutableLiveData<Propety_list>
    init {
        liked = MutableLiveData()
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

    fun unlikedProperties(property_id:Int,context: Activity){
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




}
package com.example.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Retro.RetroInstance
import com.example.Retro.RetroService
import com.example.data.BookingData_List
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookingActivityViewModel: ViewModel() {
    lateinit var bookings: MutableLiveData<BookingData_List>
    lateinit var response_delete: MutableLiveData<Int>
    init {
        bookings = MutableLiveData()
        response_delete = MutableLiveData()
    }

    fun get_booking(token: String){
        var retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
        val call = retroInstance.get_bookings(token)

        call.enqueue(object: Callback<BookingData_List> {
            override fun onFailure(call: Call<BookingData_List>, t: Throwable) {
                println("SDADADSA")
            }

            override fun onResponse(call: Call<BookingData_List>, response: Response<BookingData_List>) {
                if(response.isSuccessful) {
                    if (response.code().toInt() == 200) {
                        bookings.postValue(response.body())
                    }
                } else
                {
                    bookings.postValue(null)
                    print("response_not_succesful")
                }
            }
        })
    }

    fun delete_booking(token: String,booking_id: Int){
        var retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
        val call = retroInstance.delete_booking(booking_id,token)

        call.enqueue(object: Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                response_delete.postValue(null)
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    if (response.code().toInt() == 200) {
                        response_delete.postValue(response.code())
                    }
                } else
                {
                    response_delete.postValue(response.code())
                    print("response_not_succesful")
                }
            }
        })
    }


}
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
    init {
        bookings = MutableLiveData()
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
}
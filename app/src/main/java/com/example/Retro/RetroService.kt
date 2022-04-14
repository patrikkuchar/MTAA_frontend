package com.example.Retro

import com.example.data.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface RetroService {

    @POST("user/login/")
    @Headers("Content-Type: application/json")
    fun loginUser(@Body params: LoginRequestData): Call<LoginResponseData>

    @POST("user/register/")
    @Headers("Content-Type: application/json")
    fun registerUser(@Body params: RegisterRequestData): Call<RegisterResponseData>

    @GET("filter/{region_id}+{subregion_id}+{price_min_max}+{area_min_max}+{rooms}/")
    @Headers("Content-Type: application/json")
    fun filter(
        @Path("region_id") region_id: String,
        @Path("subregion_id") subregion_id: String,
        @Path("price_min_max") price_min_max: String,
        @Path("area_min_max") area_min_max: String,
        @Path("rooms") rooms: String,
        @Header("Authorization") token: String)
    : Call<Propety_list>

    @GET("property/{id}/")
    @Headers("Content-Type: application/json")
    fun get_property(
        @Path("id") id: Int,
        @Header("Authorization") token: String)
    :Call<PropertyInfoDataOne>

    @GET("subregions/{region_id}/")
    fun get_subregions(
        @Path("region_id") region_id: Int,
        @Header("Authorization") token: String)
    :Call<Subregion_list>

    @POST("booking/")
    fun add_booking(
        @Body params: AddBookingData,
        @Header("Authorization") token: String)
    :Call<String>


    @GET("booking/")
    fun get_bookings(
        @Header("Authorization") token: String)
    :Call<BookingData_List>

    @POST("liked/")
    fun add_like(
        @Body params: Add_Liked_Request,
        @Header("Authorization") token: String)
    : Call<String>


    @DELETE("liked/{LikedId}/delete")
    fun delete_like(
        @Path("LikedId") LikedId: Int,
        @Header("Authorization") token: String)
    : Call<String>


    @GET("liked/")
    fun get_liked(
        @Header("Authorization") token: String)
    : Call<Propety_list>


    @GET("regions/")
    @Headers("Content-Type: application/json")
    fun get_regions(
        @Header("Authorization") token: String)
    : Call<Region_List>

    //edit password
    @PATCH("user/edit/")
    fun edit_password(
        @Body params: Edit_Password_Request,
        @Header("Authorization") token: String)
    : Call <ChangePasswordResponse>

    @DELETE("booking/{booking_id}/delete")
    fun delete_booking(
        @Path("booking_id") booking_id: Int,
        @Header("Authorization") token: String)
    : Call <String>


}
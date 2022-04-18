package com.example.data

data class EditPropertyRequest(
    val rooms: Int,
    val area : Int,
    val price : Int,
    val subregion_id : Int,
    val address: String,
    val info: String,
)

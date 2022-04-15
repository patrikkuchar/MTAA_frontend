package com.example.data

data class SellPropertyRequest(
    val rooms: Int,
    val area: Int,
    val price: Int,
    val subregion: Int,
    val address: String,
    val info: String,
    val title_image: Int,
    val images: List<String>
)

package com.example.data

data class PropertyInfoData(
    val id: Int,
    val rooms: Int,
    val area: Int,
    val price: Int,
    val region: String,
    val subregion: String,
    val last_updated: String,
    val address: String,
    val info: String,
    val owner: String,
    val owner_id: Int
   // val images: List<String>
)

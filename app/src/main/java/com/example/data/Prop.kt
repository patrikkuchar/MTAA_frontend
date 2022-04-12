package com.example.data

data class Prop(
    val id: Number,
    val rooms: Number,
    val area: Number,
    val price: Number,
    val last_updated: String,
    val address: String,
    val owner: String,
    val image: String,
    var liked: Boolean? = false
    )

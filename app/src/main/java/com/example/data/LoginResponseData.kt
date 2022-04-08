package com.example.data

data class LoginResponseData(
    val email: String,
    val id: Int,
    val name: String,
    val surname: String,
    val token: String
)
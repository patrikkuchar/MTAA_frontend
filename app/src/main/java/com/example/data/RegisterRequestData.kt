package com.example.data

data class RegisterRequestData(
    val email: String?,
    val name: String?,
    val surname: String?,
    val password: String?,
    val confpassword: String
)

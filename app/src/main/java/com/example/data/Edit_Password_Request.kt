package com.example.data

data class Edit_Password_Request(
    val old_password: String,
    val new_password: String,
    val new_password_confirm: String
)

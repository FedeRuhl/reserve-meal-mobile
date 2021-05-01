package com.example.reservemeal.requests

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val dni: String
)

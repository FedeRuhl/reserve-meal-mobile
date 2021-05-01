package com.example.reservemeal.requests

data class ResetRequest(
    val email: String,
    val password: String,
    val password_confirmation: String,
    val code: String
)

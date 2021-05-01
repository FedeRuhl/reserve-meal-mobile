package com.example.reservemeal.requests

data class StoreReservationRequest(
    val scheduled_date: String,
    val product_id: Int,
    val quantity: Int,
    val amount: Float
)

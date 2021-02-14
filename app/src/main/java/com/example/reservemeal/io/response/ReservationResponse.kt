package com.example.reservemeal.io.response

import com.example.reservemeal.models.Reserve

data class ReservationResponse(
    val success:Boolean,
    val message:String,
    val reserves:ArrayList<Reserve>
)

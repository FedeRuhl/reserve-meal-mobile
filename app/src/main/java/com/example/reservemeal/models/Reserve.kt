package com.example.reservemeal.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class Reserve(
    val id:Int,
    @SerializedName("created_at")val createdAt:String,
    @SerializedName("updated_at")val updatedAt:String,
    @SerializedName("scheduled_date")val scheduledDate:String,
    val amount:Float,
    val userId:Int,
    val productId:Int,
    val quantity:Int,
    val product:Product
    )
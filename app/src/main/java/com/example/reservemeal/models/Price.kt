package com.example.reservemeal.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class Price(
    val id: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("date_until") val dateUntil: String,
    val price: Float,
    @SerializedName("product_id") val productId: Int
)

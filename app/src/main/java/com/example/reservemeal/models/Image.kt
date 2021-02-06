package com.example.reservemeal.models

import com.google.gson.annotations.SerializedName

data class Image(
    val id:Int,
    @SerializedName("product_id")val productId:Int,
    @SerializedName("product_image")val productImage: String,
    @SerializedName("created_at")val createdAt:String,
    @SerializedName("updated_at")val updatedAt:String
    )

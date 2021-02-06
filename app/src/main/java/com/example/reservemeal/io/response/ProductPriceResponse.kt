package com.example.reservemeal.io.response

import com.example.reservemeal.models.Price
import com.google.gson.annotations.SerializedName

data class ProductPriceResponse(
    val success:Boolean,
    val message:String,
    @SerializedName("product_price") val productPrice: Price
    )

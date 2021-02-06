package com.example.reservemeal.io.response

import com.example.reservemeal.models.Product

    data class ProductResponse(val success:Boolean, val message:String, val product: Product)

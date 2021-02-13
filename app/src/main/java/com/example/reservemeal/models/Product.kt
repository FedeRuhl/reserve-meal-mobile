package com.example.reservemeal.models

import java.text.SimpleDateFormat
import java.util.*

class Product(
    val id:Int,
    val name:String,
    val description:String,
    val stock:Int,
    val images:ArrayList<Image>,
    val prices:ArrayList<Price>
    ){
    fun getLastPrice():Float
    {
        var lastPrice = 0.0F
        if (prices.size > 0)
        {
            lastPrice = prices.firstOrNull {
                val today = Date()
                val dateUntil = SimpleDateFormat("yyyy-MM-dd").parse(it.dateUntil)
                dateUntil?.after(today) ?: false
            }?.price ?: 0.0F
        }
        return lastPrice
    }
}

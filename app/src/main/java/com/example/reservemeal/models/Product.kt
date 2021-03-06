package com.example.reservemeal.models

import android.content.res.Resources
import com.example.reservemeal.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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

    fun getImagesLinks():ArrayList<String>
    {
        val array: ArrayList<String> = ArrayList()
        images.forEach {
            //val baseUrl = Resources.getSystem().getString(R.string.menu_home)
            val baseUrl = "http://192.168.1.9/reserve-meal/public/"
            val link = if (it.productImage.contains("http")) it.productImage else baseUrl + it.productImage
            array.add(link)
        }
        return array
    }
}

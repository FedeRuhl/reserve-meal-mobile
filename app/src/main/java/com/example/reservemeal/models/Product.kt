package com.example.reservemeal.models

import java.util.ArrayList

data class Product(
    val id:Int,
    val name:String,
    val description:String,
    val stock:Int,
    val images:ArrayList<Image>,
    val prices:ArrayList<Price>
    )

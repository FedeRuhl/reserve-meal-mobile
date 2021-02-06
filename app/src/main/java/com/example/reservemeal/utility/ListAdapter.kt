package com.example.reservemeal.utility


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.reservemeal.R
import com.example.reservemeal.models.Product
import kotlinx.android.synthetic.main.list_element.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class ListAdapter(): RecyclerView.Adapter<ListAdapter.ViewHolder>(){
     var list = ArrayList<Product>()

     class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
          fun bind(element: Product) = with(itemView){
               tvProductName.text = element.name
               tvProductPrice.text =
                    String.format(resources.getString(R.string.price_product),
                         if (element.prices.size > 0)
                         {
                              element.prices.firstOrNull {
                                   val today = Date()
                                   val dateUntil = SimpleDateFormat("yyyy-MM-dd").parse(it.dateUntil)
                                   dateUntil?.after(today) ?: false
                              }?.price ?: 0.0
                         }
                         else 0.0
                    )
               tvProductDescription.text = element.description
               tvProductStatus.text = if (element.stock > 0) "Available" else "Not available"
          }
     }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
          return ViewHolder(
               LayoutInflater.from(parent.context).inflate(
                    R.layout.list_element,
                    parent,
                    false
               )
          )
     }

     override fun onBindViewHolder(holder: ViewHolder, position: Int) {
          val element = list[position]
          holder.bind(element)
     }

     override fun getItemCount() = list.size
}
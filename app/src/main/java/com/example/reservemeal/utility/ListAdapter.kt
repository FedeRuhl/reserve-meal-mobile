package com.example.reservemeal.utility


import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.reservemeal.R
import com.example.reservemeal.models.Price
import com.example.reservemeal.models.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_element.*
import kotlinx.android.synthetic.main.list_element.view.*
import java.net.URI
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
                         element.getLastPrice()
                    )
               tvProductDescription.text = element.description
               tvProductStatus.text = if (element.stock > 0) "Available" else "Not available"
               Picasso.get().isLoggingEnabled = true
               if (element.images.isNotEmpty())
               {
                    val url = if (element.images[0].productImage.contains("http")) element.images[0].productImage else resources.getString(R.string.base_url) + element.images[0].productImage
                    Picasso.get().load(url).into(iconImageView)
               }
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
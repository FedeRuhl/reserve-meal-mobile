package com.example.reservemeal.utility


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.reservemeal.R
import kotlinx.android.synthetic.main.list_element.view.*


class ListAdapter(): RecyclerView.Adapter<ListAdapter.ViewHolder>(){
     var list = ArrayList<ListElement>()

     class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
          fun bind(element: ListElement) = with(itemView){
               tvProductName.text = element.name
               tvProductPrice.text = String.format(resources.getString(R.string.price_product), element.price)
               tvProductDescription.text = element.description
               tvProductStatus.text = if (element.status) "Available" else "Not available"
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
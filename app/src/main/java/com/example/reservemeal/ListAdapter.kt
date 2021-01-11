package com.example.reservemeal

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import androidx.transition.Fade
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.list_element.view.*

class ListAdapter(): RecyclerView.Adapter<ListAdapter.ViewHolder>(){
     var list = ArrayList<ListElement>()

     class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
          fun bind(element: ListElement) = with(itemView){
               tvProductName.text = element.name
               tvProductPrice.text = element.price.toString()
               tvProductStatus.text = element.status.toString()
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
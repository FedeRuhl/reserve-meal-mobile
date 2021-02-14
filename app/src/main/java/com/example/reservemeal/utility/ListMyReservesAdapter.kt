package com.example.reservemeal.utility

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.reservemeal.R
import com.example.reservemeal.models.Reserve
import kotlinx.android.synthetic.main.list_element.view.tvProductName
import kotlinx.android.synthetic.main.list_element.view.tvProductPrice
import kotlinx.android.synthetic.main.list_my_reserves.view.*

class ListMyReservesAdapter(): RecyclerView.Adapter<ListMyReservesAdapter.ViewHolder>() {
    var list = ArrayList<Reserve>()
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(element: Reserve) = with(itemView){
            tvProductName.text = element.product.name
            tvProductPrice.text = String.format(resources.getString(R.string.price_product), element.amount)
            tvScheduledDate.text = element.scheduledDate.subSequence(0, element.scheduledDate.length-3) //date without seconds
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_my_reserves,
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
package com.example.reservemeal.utility

import android.graphics.Paint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.reservemeal.R
import com.example.reservemeal.models.Reserve
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_element.view.*
import kotlinx.android.synthetic.main.list_element.view.tvProductName
import kotlinx.android.synthetic.main.list_element.view.tvProductPrice
import kotlinx.android.synthetic.main.list_my_reserves.view.*
import kotlinx.android.synthetic.main.list_my_reserves.view.iconImageView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ListMyReservesAdapter() : RecyclerView.Adapter<ListMyReservesAdapter.ViewHolder>() {
    var list = ArrayList<Reserve>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(element: Reserve) = with(itemView) {
            tvProductName.text = element.product.name
            tvProductPrice.text = String.format(
                resources.getString(R.string.price_product),
                element.amount
            )
            tvScheduledDate.text = element.scheduledDate.subSequence(
                0,
                element.scheduledDate.length - 3
            ) //date without seconds

            val date = LocalDateTime.parse(
                element.scheduledDate,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            )
            if (date.isBefore(LocalDateTime.now()))
                tvScheduledDate.paintFlags =
                    tvScheduledDate.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            Picasso.get().isLoggingEnabled = true
            if (element.product.images.isNotEmpty()) {
                val url =
                    if (element.product.images[0].productImage.contains("http")) element.product.images[0].productImage else resources.getString(
                        R.string.base_url
                    ) + element.product.images[0].productImage
                Picasso.get().load(url).into(iconImageView)
            }
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val element = list[position]
        holder.bind(element)
    }

    override fun getItemCount() = list.size
}
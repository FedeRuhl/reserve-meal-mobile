package com.example.reservemeal.utility


import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.example.reservemeal.R
import com.squareup.picasso.Picasso


class ViewPagerAdapter internal constructor(
    private val context: Context,
    private val imageUrls: ArrayList<String>
) :
    PagerAdapter() {
    override fun getCount(): Int {
        return imageUrls.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(context)
        Picasso.get()
            .load(imageUrls[position])
            .error(R.drawable.food)
            .fit()
            .centerCrop()
            .into(imageView)
        container.addView(imageView)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
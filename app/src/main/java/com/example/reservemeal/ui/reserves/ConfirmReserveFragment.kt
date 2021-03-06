package com.example.reservemeal.ui.reserves

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.reservemeal.R
import com.example.reservemeal.io.ApiService
import com.example.reservemeal.io.response.FundsResponse
import com.example.reservemeal.utility.PreferenceHelper
import com.example.reservemeal.utility.ViewPagerAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_add_funds.*
import kotlinx.android.synthetic.main.fragment_confirm_reserve.*
import kotlinx.android.synthetic.main.fragment_confirm_reserve.tvProductName
import kotlinx.android.synthetic.main.fragment_confirm_reserve.tvProductPrice
import kotlinx.android.synthetic.main.list_element.view.*
import kotlinx.android.synthetic.main.list_my_reserves.*
import retrofit2.Call
import retrofit2.Response
import java.lang.Double.parseDouble
import javax.security.auth.callback.Callback

class ConfirmReserveFragment : Fragment() {
    private val preferences by lazy{
        PreferenceHelper.defaultPrefs(requireActivity())
    }

    private val apiService: ApiService by lazy {
        ApiService.create()
    }
    private lateinit var confirmReserveViewModel: ConfirmReserveViewModel
    private lateinit var datePicked:String
    private lateinit var productId:String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        confirmReserveViewModel =
            ViewModelProvider(requireActivity()).get(ConfirmReserveViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_confirm_reserve, container, false)
        //val textView: TextView = root.findViewById(R.id.text_slideshow)
        /*confirmReserveViewModel.text.observe(viewLifecycleOwner, Observer {
            //  textView.text = it
        })*/
        return root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val productName = arguments?.getString("productName")
        val productPrice = arguments?.getString("productPrice")
        productId = arguments?.getString("productId") ?: "NULL"
        val productImages = arguments?.getStringArrayList("productImages") ?: ArrayList()
        getImages(productImages)
        tvProductName.text = productName
        tvProductPrice.text = productPrice
        btnPickDate.setOnClickListener {
            var c = Calendar.getInstance()
            var day = c.get(Calendar.DAY_OF_MONTH)
            var month = c.get(Calendar.MONTH)
            var year = c.get(Calendar.YEAR)
            var dialog =
                context?.let { it1 ->
                    DatePickerDialog(it1, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        val timePickerDialog = TimePickerDialog(it1, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                            datePicked = "$year-${month+1}-$dayOfMonth $hourOfDay:$minute"
                        }, Calendar.HOUR, Calendar.MINUTE, DateFormat.is24HourFormat(it1)).show()
                    }, year, month, day)
                }
            dialog?.show()
        }
        etProductQuantity.doAfterTextChanged{
            val quantity = it.toString().toFloatOrNull()
            if (quantity != null)
            {
                val price = productPrice?.toFloat() ?: 0.0F
                tvProductPrice.text = (price * quantity).toString()
                //Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
        }
        btnConfirm.setOnClickListener {
            performConfirm()
        }
    }

    private fun getImages(productImages: ArrayList<String>) {
            val adapter = ViewPagerAdapter(requireActivity(), productImages)
            view_pager.adapter = adapter
    }

    private fun performConfirm() {
        val jwt = preferences.getString("jwt", "")
        val call = apiService.postReservation("Bearer $jwt", datePicked, productId.toInt(), etProductQuantity.text.toString().toInt(), tvProductPrice.text.toString().toFloat())
        call.enqueue(object: retrofit2.Callback<FundsResponse> {
            override fun onFailure(call: Call<FundsResponse>, t: Throwable) {
                Toast.makeText(requireActivity(), t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<FundsResponse>, response: Response<FundsResponse>) {
                if (response.isSuccessful && response.body()?.success == true)
                {
                    response.body().let {
                        Toast.makeText(requireActivity(), it?.message, Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_confirmReserveFragment_to_nav_my_reserves)
                    }
                }
                else
                {
                    Toast.makeText(requireActivity(), response.body()?.message ?: response.errorBody().toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
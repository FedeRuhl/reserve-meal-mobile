package com.example.reservemeal.ui.reserves

import android.annotation.SuppressLint
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
import com.example.reservemeal.requests.StoreReservationRequest
import com.example.reservemeal.utility.PreferenceHelper
import com.example.reservemeal.utility.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_confirm_reserve.*
import kotlinx.android.synthetic.main.fragment_confirm_reserve.tvProductName
import kotlinx.android.synthetic.main.fragment_confirm_reserve.tvProductPrice
import retrofit2.Call
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ConfirmReserveFragment : Fragment() {
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(requireActivity())
    }

    private val apiService: ApiService by lazy {
        ApiService.create()
    }
    private lateinit var confirmReserveViewModel: ConfirmReserveViewModel
    private lateinit var datePicked: String
    private lateinit var productId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        confirmReserveViewModel =
            ViewModelProvider(requireActivity()).get(ConfirmReserveViewModel::class.java)
        return inflater.inflate(R.layout.fragment_confirm_reserve, container, false)
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val productName = arguments?.getString("productName")
        val productPrice = arguments?.getString("productPrice")
        productId = arguments?.getString("productId") ?: "NULL"
        val productImages = arguments?.getStringArrayList("productImages") ?: ArrayList()
        getImages(productImages)
        tvProductName.text = productName
        tvProductPrice.text = productPrice
        btnPickDate.setOnClickListener {
            val c = Calendar.getInstance()
            val day = c.get(Calendar.DAY_OF_MONTH)
            val month = c.get(Calendar.MONTH)
            val year = c.get(Calendar.YEAR)
            val dialog =
                context?.let { it1 ->
                    DatePickerDialog(
                        it1,
                        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                            val timePickerDialog = TimePickerDialog(
                                it1,
                                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                                    val monthString =
                                        if ((month + 1).toString().length == 1) "0${month + 1}" else (month + 1).toString()
                                    val dayString =
                                        if ((dayOfMonth).toString().length == 1) "0$dayOfMonth" else (dayOfMonth).toString()
                                    val hourString =
                                        if ((hourOfDay).toString().length == 1) "0$hourOfDay" else (hourOfDay).toString()
                                    val minuteString =
                                        if ((minute).toString().length == 1) "0$minute" else (minute).toString()
                                    datePicked =
                                        "$year-$monthString-$dayString $hourString:$minuteString"
                                },
                                Calendar.HOUR,
                                Calendar.MINUTE,
                                DateFormat.is24HourFormat(it1)
                            ).show()
                        },
                        year,
                        month,
                        day
                    )
                }
            dialog?.show()
        }
        etProductQuantity.doAfterTextChanged {
            val quantity = it.toString().toFloatOrNull()
            if (quantity != null) {
                val price = productPrice?.toFloat() ?: 0.0F
                tvProductPrice.text = (price * quantity).toString()
            }
        }
        btnConfirm.setOnClickListener {
            if (this::datePicked.isInitialized && datePicked.isNotEmpty()) {
                val date =
                    LocalDateTime.parse(datePicked, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                if (date.isAfter(LocalDateTime.now()))
                    performConfirm()
                else
                    Toast.makeText(
                        requireActivity(),
                        "The selected date must be greater than the current date",
                        Toast.LENGTH_LONG
                    ).show()
            } else
                Toast.makeText(
                    requireActivity(),
                    "Please, select a date before confirming",
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    private fun getImages(productImages: ArrayList<String>) {
        val images =
            if (productImages.isEmpty()) arrayListOf(R.drawable.food.toString()) else productImages

        val adapter = ViewPagerAdapter(requireActivity(), images)
        view_pager.adapter = adapter
    }

    private fun performConfirm() {
        val jwt = preferences.getString("jwt", "")
        val storeReservationRequest = StoreReservationRequest(
            datePicked,
            productId.toInt(),
            etProductQuantity.text.toString().toInt(),
            tvProductPrice.text.toString().toFloat()
        )
        val call = apiService.postReservation("Bearer $jwt", storeReservationRequest)
        call.enqueue(object : retrofit2.Callback<FundsResponse> {
            override fun onFailure(call: Call<FundsResponse>, t: Throwable) {
                Toast.makeText(requireActivity(), t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<FundsResponse>, response: Response<FundsResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body().let {
                        Toast.makeText(requireActivity(), it?.message, Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_confirmReserveFragment_to_nav_my_reserves)
                    }
                } else {
                    Toast.makeText(
                        requireActivity(),
                        response.body()?.message ?: response.errorBody().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}
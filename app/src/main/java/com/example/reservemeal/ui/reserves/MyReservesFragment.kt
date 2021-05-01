package com.example.reservemeal.ui.reserves

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reservemeal.*
import com.example.reservemeal.io.ApiService
import com.example.reservemeal.io.response.ReservationResponse
import com.example.reservemeal.utility.ListMyReservesAdapter
import com.example.reservemeal.utility.PreferenceHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyReservesFragment : Fragment() {

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(requireActivity())
    }

    private val apiService: ApiService by lazy {
        ApiService.create()
    }
    private lateinit var myReservesViewModel: MyReservesViewModel
    private val listAdapter = ListMyReservesAdapter()
    private lateinit var listRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loadReserves()
        myReservesViewModel =
            ViewModelProvider(requireActivity()).get(MyReservesViewModel::class.java)
        return inflater.inflate(R.layout.fragment_my_reserves, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listRecyclerView = view.findViewById(R.id.myReservesRecyclerView)
        listRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        listRecyclerView.adapter = listAdapter
        super.onViewCreated(view, savedInstanceState)
    }

    private fun loadReserves() {
        val jwt = preferences.getString("jwt", "")
        val call = apiService.getMyReservations("Bearer $jwt")
        call.enqueue(object : Callback<ReservationResponse> {
            override fun onFailure(call: Call<ReservationResponse>, t: Throwable) {
                Toast.makeText(requireActivity(), t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ReservationResponse>,
                response: Response<ReservationResponse>
            ) {
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.let {
                        if (it.reserves.size > 0) {
                            listAdapter.list = it.reserves
                            listAdapter.notifyDataSetChanged()
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                "There is not reserves yet",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
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
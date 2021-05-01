package com.example.reservemeal.ui.funds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.reservemeal.R
import com.example.reservemeal.io.ApiService
import com.example.reservemeal.io.response.FundsResponse
import com.example.reservemeal.requests.AddFundsRequest
import com.example.reservemeal.utility.PreferenceHelper
import kotlinx.android.synthetic.main.fragment_add_funds.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddFundsFragment : Fragment() {

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(requireActivity())
    }

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private lateinit var addFundsViewModel: AddFundsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addFundsViewModel =
            ViewModelProviders.of(this).get(AddFundsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_add_funds, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btnAddFunds.setOnClickListener {
            if (etDNI.text.isNotEmpty() && etAmount.text.isNotEmpty() && etReAmount.text.isNotEmpty()
                && etAmount.text.toString().toFloatOrNull() != null && etReAmount.text.toString()
                    .toFloatOrNull() != null
            )
                addFunds()
            else
                Toast.makeText(requireActivity(), R.string.complete_all_fields, Toast.LENGTH_LONG)
                    .show()
        }
    }

    private fun addFunds() {
        val amount = etAmount.text.toString()
        val reAmount = etReAmount.text.toString()
        if (amount == reAmount) {
            val jwt = preferences.getString("jwt", "")
            val addFundsRequest = AddFundsRequest(etDNI.text.toString(), amount.toFloat())
            val call = apiService.addFunds("Bearer $jwt", addFundsRequest)
            call.enqueue(object : Callback<FundsResponse> {
                override fun onFailure(call: Call<FundsResponse>, t: Throwable) {
                    Toast.makeText(requireActivity(), t.localizedMessage, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<FundsResponse>,
                    response: Response<FundsResponse>
                ) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        response.body().let {
                            Toast.makeText(requireActivity(), it?.message, Toast.LENGTH_SHORT)
                                .show()
                            etDNI.text.clear()
                            etAmount.text.clear()
                            etReAmount.text.clear()
                            etDNI.requestFocus()
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
        } else {
            Toast.makeText(
                requireActivity(),
                "The amount and amount confirmation must be equals",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}
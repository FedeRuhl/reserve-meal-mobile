package com.example.reservemeal.ui.funds

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.reservemeal.R
import com.example.reservemeal.io.ApiService
import com.example.reservemeal.io.response.FundsResponse
import com.example.reservemeal.utility.PreferenceHelper
import kotlinx.android.synthetic.main.fragment_add_funds.*
import kotlinx.android.synthetic.main.fragment_create_meal.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddFundsFragment : Fragment() {

    private val preferences by lazy{
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
        val root = inflater.inflate(R.layout.fragment_add_funds, container, false)
        //val textView: TextView = root.findViewById(R.id.text_slideshow)
        addFundsViewModel.text.observe(viewLifecycleOwner, Observer {
            //  textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btnAddFunds.setOnClickListener {
            addFunds()
        }
    }

    private fun addFunds() {
        var amount = etAmount.text.toString()
        var reAmount = etReAmount.text.toString()
        if (amount == reAmount)
        {
            val jwt = preferences.getString("jwt", "")
            /*val data = getPayloadJWT(jwt ?: "")
            val obj = JSONObject(data)
            var userId = obj["uid"].toString().toInt()*/
            val call = apiService.addFunds("Bearer $jwt", etDNI.text.toString(), amount.toFloat())
            call.enqueue(object: Callback<FundsResponse>{
                override fun onFailure(call: Call<FundsResponse>, t: Throwable) {
                    Toast.makeText(requireActivity(), t.localizedMessage, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<FundsResponse>, response: Response<FundsResponse>) {
                    if (response.isSuccessful && response.body()?.success == true)
                    {
                        response.body().let {
                            Toast.makeText(requireActivity(), it?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    else
                    {
                        Toast.makeText(requireActivity(), response.body()?.message ?: response.errorBody().toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
        else
        {
            Toast.makeText(requireActivity(), "The amount and amount confirmation must be equals", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getPayloadJWT(jwt:String):String {
        return if (jwt.isNotEmpty()) {
            var array = jwt.split(".")
            var decode = Base64.decode(array[1], Base64.URL_SAFE)
            String(decode, Charsets.UTF_8)
        } else
            ""
    }
}
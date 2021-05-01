package com.example.reservemeal.ui.home

import android.media.Image
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reservemeal.R
import com.example.reservemeal.io.ApiService
import com.example.reservemeal.models.Product
import com.example.reservemeal.ui.funds.AddFundsFragment
import com.example.reservemeal.ui.reserves.ConfirmReserveFragment
import com.example.reservemeal.utility.ListAdapter
import com.example.reservemeal.utility.PreferenceHelper
import com.example.reservemeal.utility.RecyclerTouchListener
import com.example.reservemeal.utility.getPayloadValue
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_element.*
import kotlinx.android.synthetic.main.nav_header_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class HomeFragment : Fragment() {

    private val listAdapter = ListAdapter()
    private lateinit var listRecyclerView: RecyclerView

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(requireActivity())
    }

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init()
        homeViewModel =
            ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listRecyclerView = view.findViewById(R.id.listRecyclerView)
        listRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        listRecyclerView.adapter = listAdapter

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listRecyclerView.addOnItemTouchListener(
            RecyclerTouchListener(
                activity, //requireActivity()
                listRecyclerView,
                object : RecyclerTouchListener.ClickListener {
                    override fun onClick(view: View?, position: Int) {
                        val item = listAdapter.list[position]
                        val args = Bundle()
                        args.putString("productId", item.id.toString())
                        args.putString("productName", item.name)
                        args.putString("productPrice", item.getLastPrice().toString())
                        args.putStringArrayList("productImages", item.getImagesLinks())
                        findNavController().navigate(
                            R.id.action_nav_home_to_confirmReserveFragment,
                            args
                        )
                    }

                    override fun onLongClick(view: View?, position: Int) {}
                })
        )
    }

    private fun init() {
        loadProducts()
    }

    private fun loadProducts() {
        val jwt = preferences.getString("jwt", "")
        val call = apiService.getProducts("Bearer $jwt")
        call.enqueue(object : Callback<ArrayList<Product>> {
            override fun onFailure(call: Call<ArrayList<Product>>, t: Throwable) {
                Toast.makeText(requireActivity(), t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ArrayList<Product>>,
                response: Response<ArrayList<Product>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.isNotEmpty()) {
                            listAdapter.list = it
                            listAdapter.notifyDataSetChanged()
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                "There is not products yet",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        })
    }
}
package com.example.reservemeal.ui.ui.reserves

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reservemeal.*

class MyReservesFragment : Fragment() {

    private lateinit var myReservesViewModel: MyReservesViewModel
    private val listAdapter = ListMyReservesAdapter()
    private lateinit var listRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init()
        myReservesViewModel =
            ViewModelProvider(requireActivity()).get(MyReservesViewModel::class.java)
            //ViewModelProviders.of(this).get(MyReservesViewModel::class.java)
        return inflater.inflate(R.layout.fragment_my_reserves, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listRecyclerView = view.findViewById(R.id.myReservesRecyclerView)
        listRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        listRecyclerView.adapter =listAdapter
        super.onViewCreated(view, savedInstanceState)
    }

    private fun init() {
        val elements = ArrayList<ListMyReserves>()
        elements.add(ListMyReserves("Sanguche milanesa", 100.0, "01/02/2021", "15:00"))
        listAdapter.list = elements
    }
}
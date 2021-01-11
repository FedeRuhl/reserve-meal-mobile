package com.example.reservemeal.ui.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reservemeal.ListAdapter
import com.example.reservemeal.ListElement
import com.example.reservemeal.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private val listAdapter = ListAdapter()

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init()
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        listRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        listRecyclerView.adapter =listAdapter
        super.onActivityCreated(savedInstanceState)
    }

    private fun init() {
        val elements = ArrayList<ListElement>()
        elements.add(ListElement("Sanguche milanesa", 250.0, true))
        elements.add(ListElement("Milanesa con papas", 350.0, true))
        elements.add(ListElement("Ensalada", 250.0, true))
        listAdapter.list = elements
    }
}
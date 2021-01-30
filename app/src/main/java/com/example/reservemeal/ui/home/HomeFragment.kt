package com.example.reservemeal.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reservemeal.utility.ListAdapter
import com.example.reservemeal.utility.ListElement
import com.example.reservemeal.R

class HomeFragment : Fragment() {

    private val listAdapter = ListAdapter()
    private lateinit var listRecyclerView: RecyclerView

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
        //val textView: TextView = view.findViewById(R.id.text_gallery)
        /*homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
        listRecyclerView = view.findViewById(R.id.listRecyclerView)
        listRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        listRecyclerView.adapter =listAdapter
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun init() {
        val elements = ArrayList<ListElement>()
        elements.add(ListElement("Sanguche milanesa", "Main food",250.0, true))
        elements.add(ListElement("Milanesa con papas", "Second food", 350.0, true))
        elements.add(ListElement("Ensalada", "Third food",250.0, true))
        listAdapter.list = elements
    }
}
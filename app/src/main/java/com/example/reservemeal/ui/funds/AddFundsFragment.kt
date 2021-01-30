package com.example.reservemeal.ui.funds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.reservemeal.R

class AddFundsFragment : Fragment() {

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
}
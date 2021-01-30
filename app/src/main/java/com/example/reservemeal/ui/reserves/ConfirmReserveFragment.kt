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
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.reservemeal.R
import kotlinx.android.synthetic.main.fragment_confirm_reserve.*

class ConfirmReserveFragment : Fragment() {

    private lateinit var confirmReserveViewModel: ConfirmReserveViewModel

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
        btnPickDate.setOnClickListener {
            var c = Calendar.getInstance()
            var day = c.get(Calendar.DAY_OF_MONTH)
            var month = c.get(Calendar.MONTH)
            var year = c.get(Calendar.YEAR)
            var dialog =
                context?.let { it1 ->
                    DatePickerDialog(it1, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        val timePickerDialog = TimePickerDialog(it1, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                            testing.text = testing.text.toString() + " " +"$hourOfDay:$minute"
                        }, Calendar.HOUR, Calendar.MINUTE, DateFormat.is24HourFormat(it1)).show()
                        testing.text = "$year-${month+1}-$dayOfMonth"
                    }, year, month, day)
                }
            dialog?.show()
        }
    }
}
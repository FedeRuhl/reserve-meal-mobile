package com.example.reservemeal.ui.ui.meal

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.reservemeal.R
import kotlinx.android.synthetic.main.fragment_create_meal.*

class CreateMealFragment : Fragment() {

    private lateinit var createMealViewModel: CreateMealViewModel
    private var images: ArrayList<Uri?>? = null
    private var PICK_IMAGES_CODE = 0

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        createMealViewModel =
            ViewModelProviders.of(this).get(CreateMealViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_create_meal, container, false)
        //val textView: TextView = root.findViewById(R.id.text_gallery)
        createMealViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it
        })
        return root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btnAddImage.setOnClickListener {
            pickImagesIntent()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private fun pickImagesIntent(){
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select image/s"), PICK_IMAGES_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGES_CODE){
            if (resultCode == Activity.RESULT_OK){
                if (data?.clipData != null){
                    //picked multiple images
                    val count = data.clipData?.itemCount
                    for (i in 0 until count!!){
                        val imageUri = data.clipData?.getItemAt(i)?.uri
                        images?.add(imageUri!!)
                    }
                }
                else{
                    //picked single image
                    val imageUri = data?.data
                    images?.add(imageUri!!)
                }
            }
        }
    }
}
package com.example.reservemeal.ui.meal

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.reservemeal.R
import com.example.reservemeal.io.ApiService
import com.example.reservemeal.io.response.ProductPriceResponse
import com.example.reservemeal.io.response.ProductResponse
import com.example.reservemeal.ui.session.MainActivity2
import com.example.reservemeal.utility.PreferenceHelper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_create_meal.*
import kotlinx.android.synthetic.main.list_element.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateMealFragment : Fragment() {

    private val preferences by lazy{
        PreferenceHelper.defaultPrefs(requireActivity())
    }

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

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
        btnAddMeal.setOnClickListener {
            createMeal()
        }
    }

    private fun createMeal() {
        val jwt = preferences.getString("jwt", "")
        val call = apiService.postProduct("Bearer $jwt", etName.text.toString(), etDescription.text.toString(), etStock.text.toString().toInt())
        call.enqueue(object: Callback<ProductResponse>{
            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Toast.makeText(requireActivity(), t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                if (response.isSuccessful && response.body()?.success == true)
                {
                    val postProductResponse = response.body()
                    postProductResponse?.let{
                        val productId = it.product.id
                        val price = etPrice.text.toString().toFloat()
                        createPrice(productId, price)
                    } ?: run {
                        Toast.makeText(requireActivity(), "Unauthorized. Please, try again later", Toast.LENGTH_SHORT).show()
                    }
                }
                else
                {
                    Toast.makeText(requireActivity(), response.body()?.message ?: response.errorBody().toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun createPrice(productId: Int, price: Float) {
        val jwt = preferences.getString("jwt", "")
        val call = apiService.createPrice("Bearer $jwt", price, productId)
        call.enqueue(object: Callback<ProductPriceResponse>{
            override fun onFailure(call: Call<ProductPriceResponse>, t: Throwable) {
                Toast.makeText(requireActivity(), t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ProductPriceResponse>,
                response: Response<ProductPriceResponse>
            ) {
                if (response.isSuccessful && response.body()?.success == true)
                {
                    Toast.makeText(requireActivity(), "Product successfully created", Toast.LENGTH_SHORT).show()
                    goToMainActivity2()
                }
                else
                {
                    Toast.makeText(requireActivity(), response.body()?.message ?: response.errorBody().toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
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

    private fun goToMainActivity2(){
        val intent = Intent(requireActivity(), MainActivity2::class.java)
        startActivity(intent)
    }
}
package com.example.reservemeal.ui.meal

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.reservemeal.R
import com.example.reservemeal.io.ApiService
import com.example.reservemeal.io.response.ProductPriceResponse
import com.example.reservemeal.io.response.ProductResponse
import com.example.reservemeal.io.response.UploadResponse
import com.example.reservemeal.requests.StoreProductPriceRequest
import com.example.reservemeal.requests.StoreProductRequest
import com.example.reservemeal.ui.session.HomeActivity
import com.example.reservemeal.utility.PreferenceHelper
import com.example.reservemeal.utility.getFileName
import kotlinx.android.synthetic.main.fragment_create_meal.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.*


class CreateMealFragment : Fragment() {
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(requireActivity())
    }

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private lateinit var createMealViewModel: CreateMealViewModel
    private lateinit var images: ArrayList<MultipartBody.Part>
    private var pickImagesCode = 0

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        createMealViewModel =
            ViewModelProviders.of(this).get(CreateMealViewModel::class.java)
        return inflater.inflate(R.layout.fragment_create_meal, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btnAddImage.setOnClickListener {
            pickImagesIntent()
        }
        btnAddMeal.setOnClickListener {
            if (etName.text.isNotEmpty() && etDescription.text.isNotEmpty() && etStock.text.isNotEmpty() && etPrice.text.isNotEmpty()) {
                if (this::images.isInitialized && images.isNotEmpty())
                    createMeal(withImages = true)
                else
                    createMeal()
            } else
                Toast.makeText(requireActivity(), R.string.complete_all_fields, Toast.LENGTH_LONG)
                    .show()
        }
    }


    private fun createMeal(withImages: Boolean = false) {
        val jwt = preferences.getString("jwt", "")
        val storeProductRequest = StoreProductRequest(
            etName.text.toString(),
            etDescription.text.toString(),
            etStock.text.toString().toInt()
        )
        val call = apiService.postProduct(
            "Bearer $jwt",
            storeProductRequest
        )
        call.enqueue(object : Callback<ProductResponse> {
            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Toast.makeText(requireActivity(), t.localizedMessage, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                if (response.isSuccessful && response.body()?.success == true) {
                    val postProductResponse = response.body()
                    postProductResponse?.let {
                        val productId = it.product.id
                        val price = etPrice.text.toString().toFloat()
                        createPrice(productId, price)
                        if (withImages)
                            createImages(productId)
                        else
                            goToHomeActivity()
                    } ?: run {
                        Toast.makeText(
                            requireActivity(),
                            "Unauthorized. Please, try again later",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireActivity(),
                        response.body()?.message ?: response.errorBody().toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

    private fun createImages(productId: Int) {
        val jwt = preferences.getString("jwt", "")
        val call = apiService.uploadImages("Bearer $jwt", productId, images, images.size)
        call.enqueue(object : Callback<UploadResponse> {
            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                Toast.makeText(requireActivity(), t.localizedMessage, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                if (!response.isSuccessful || response.body()?.error == true) {
                    Toast.makeText(
                        requireActivity(),
                        response.body()?.message ?: response.errorBody().toString(),
                        Toast.LENGTH_LONG
                    ).show()
                } else
                    goToHomeActivity()
            }
        })
    }

    private fun createPrice(productId: Int, price: Float) {
        val jwt = preferences.getString("jwt", "")
        val storeProductPriceRequest = StoreProductPriceRequest(price, productId)
        val call = apiService.createPrice("Bearer $jwt", storeProductPriceRequest)
        call.enqueue(object : Callback<ProductPriceResponse> {
            override fun onFailure(call: Call<ProductPriceResponse>, t: Throwable) {
                Toast.makeText(requireActivity(), t.localizedMessage, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<ProductPriceResponse>,
                response: Response<ProductPriceResponse>
            ) {
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(
                        requireActivity(),
                        "Product successfully created",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        requireActivity(),
                        response.body()?.message ?: response.errorBody().toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

    private fun pickImagesIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select image/s"), pickImagesCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickImagesCode) {
            if (resultCode == Activity.RESULT_OK) {

                if (data?.data != null) {
                    val imageType = context?.contentResolver?.getType(data.data!!)
                    val extension = imageType!!.substring(imageType.indexOf("/") + 1)
                    var name = context?.contentResolver?.getFileName(data.data!!)
                    name = name?.substring(0, name.indexOf("."))

                    context?.contentResolver?.openInputStream(data.data!!)?.use { inputStream ->
                        val filePartImage = MultipartBody.Part.createFormData(
                            "product_image_0",
                            "$name.$extension",
                            inputStream.readBytes().toRequestBody("*/*".toMediaType())
                        )
                        images = ArrayList()
                        images.add(filePartImage)
                    }
                }

                if (data?.clipData != null) {
                    images = ArrayList()
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count) {

                        val imageUri = data.clipData!!.getItemAt(i).uri
                        val imageType = context?.contentResolver?.getType(imageUri)
                        val extension = imageType!!.substring(imageType.indexOf("/") + 1)
                        var name = context?.contentResolver?.getFileName(imageUri)
                        name = name?.substring(0, name.indexOf("."))

                        context?.contentResolver?.openInputStream(imageUri)?.use { inputStream ->
                            val filePartImage = MultipartBody.Part.createFormData(
                                "product_image_$i",
                                "$name.$extension",
                                inputStream.readBytes().toRequestBody("*/*".toMediaType())
                            )
                            images.add(filePartImage)
                        }
                    }
                }
            }
        }
    }

    private fun goToHomeActivity() {
        val intent = Intent(requireActivity(), HomeActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}
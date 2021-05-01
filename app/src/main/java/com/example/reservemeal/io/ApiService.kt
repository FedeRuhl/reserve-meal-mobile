package com.example.reservemeal.io

import com.example.reservemeal.io.response.*
import com.example.reservemeal.models.Product
import com.example.reservemeal.requests.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface ApiService {
    @POST("login")
    @Headers("Accept: application/json")
    fun postLogin(
        @Body request: LoginRequest
    ): Call<LoginResponse>

    @POST("register")
    fun postRegister(
        @Body request: RegisterRequest
    ): Call<RegisterResponse>

    @POST("password/email")
    fun postForget(
        @Body request: ForgetRequest
    ): Call<ForgetResponse>

    @POST("password/reset")
    fun postReset(
        @Body request: ResetRequest
    ): Call<ForgetResponse>

    @GET("products")
    fun getProducts(
        @Header("Authorization") authHead: String
    ): Call<ArrayList<Product>>

    @POST("products/store")
    fun postProduct(
        @Header("Authorization") authHead: String,
        @Body request: StoreProductRequest
    ): Call<ProductResponse>

    @POST("productPrices/store")
    fun createPrice(
        @Header("Authorization") authHead: String,
        @Body request: StoreProductPriceRequest
    ): Call<ProductPriceResponse>

    @POST("user/add-funds")
    fun addFunds(
        @Header("Authorization") authHead: String,
        @Body request: AddFundsRequest
    ): Call<FundsResponse>

    @POST("reservations/store")
    fun postReservation(
        @Header("Authorization") authHead: String,
        @Body request: StoreReservationRequest
    ): Call<FundsResponse>

    @GET("my-reservations")
    fun getMyReservations(
        @Header("Authorization") authHead: String
    ): Call<ReservationResponse>

    @Multipart
    @POST("productImages/storeAll")
    @Headers("Accept: application/json")
    fun uploadImages(
        @Header("Authorization") authHead: String,
        @Part("product_id") productId: Int,
        @Part product_images: ArrayList<MultipartBody.Part>,
        @Part("size") size: Int
    ): Call<UploadResponse>

    companion object Factory {
        private const val BASE_URL = "http://192.168.1.9/reserve-meal/public/api/"

        fun create(): ApiService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}
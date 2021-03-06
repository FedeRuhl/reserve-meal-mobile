package com.example.reservemeal.io

import android.media.Image
import com.example.reservemeal.io.response.*
import com.example.reservemeal.models.Product
import com.example.reservemeal.models.Reserve
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface ApiService {
    @POST("login")
    @Headers("Accept: application/json")
    fun postLogin(@Query("dni") dni: String, @Query("password") password: String): Call<LoginResponse>

    @POST("register")
    fun postRegister(
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("dni") dni: String
    ): Call<RegisterResponse>

    @POST("password/email")
    fun postForget(
        @Query("email") email: String
    ): Call<ForgetResponse>

    @POST("password/reset")
    fun postReset(
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("password_confirmation") passwordConfirmation: String,
        @Query("code") code: String
    ): Call<ForgetResponse>

    @GET("products")
    fun getProducts(@Header("Authorization") authHead: String): Call<ArrayList<Product>>

    @POST("products/store")
    fun postProduct(
        @Header("Authorization") authHead: String,
        @Query("name") name: String,
        @Query("description") description: String,
        @Query("stock") stock: Int
    ): Call<ProductResponse>

    @POST("productPrices/store")
    fun createPrice(
        @Header("Authorization") authHead: String,
        @Query("price") price: Float,
        @Query("product_id") productId: Int
    ): Call<ProductPriceResponse>

    @POST("user/add-funds")
    fun addFunds(
        @Header("Authorization") authHead: String,
        @Query("dni") dni: String,
        @Query("amount") amount:Float
    ): Call <FundsResponse>

    @POST("reservations/store")
    fun postReservation(
        @Header("Authorization") authHead: String,
        @Query("scheduled_date") scheduledDate: String,
        @Query("product_id") productId: Int,
        @Query("quantity") quantity: Int,
        @Query("amount") amount: Float
    ):  Call <FundsResponse>

    @GET("my-reservations")
    fun getMyReservations(
        @Header("Authorization") authHead: String
    ): Call <ReservationResponse>

    companion object Factory{
        private const val BASE_URL = "http://192.168.1.9/reserve-meal/public/api/"

        fun create(): ApiService{
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
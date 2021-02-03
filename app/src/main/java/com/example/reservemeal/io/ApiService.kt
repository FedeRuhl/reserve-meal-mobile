package com.example.reservemeal.io

import com.example.reservemeal.io.response.LoginResponse
import com.example.reservemeal.io.response.RegisterResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("login")
    @Headers("Accept: application/json")
    fun postLogin(@Query("dni") dni:String, @Query("password") password:String): Call<LoginResponse>

    @POST("register")
    fun postRegister(@Query("name") name:String,
                     @Query("email") email:String,
                     @Query("password") password:String,
                     @Query("dni") dni:String
    ): Call<RegisterResponse>

    companion object Factory{
        private const val BASE_URL = "http://10.0.2.2/reserve-meal/public/api/"

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
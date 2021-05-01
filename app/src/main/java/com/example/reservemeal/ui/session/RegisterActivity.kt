package com.example.reservemeal.ui.session

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.reservemeal.R
import com.example.reservemeal.io.ApiService
import com.example.reservemeal.io.response.RegisterResponse
import com.example.reservemeal.requests.RegisterRequest
import com.example.reservemeal.utility.toast
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.etDNI
import kotlinx.android.synthetic.main.activity_register.etPassword
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        tvGoToSignIn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnRegister.setOnClickListener {
            performRegister()
        }
    }

    private fun clearInputs() {
        etName.text.clear()
        etEmail.text.clear()
        etDNI.text.clear()
        etPassword.text.clear()
        etPasswordConfirmation.text.clear()
    }

    private fun performRegister() {
        if (etDNI.text.isNotEmpty() && etEmail.text.isNotEmpty()
            && etName.text.isNotEmpty() && etPassword.text.isNotEmpty() && etPasswordConfirmation.text.isNotEmpty()
        ) {
            if (etPassword.text.trim().toString() == etPasswordConfirmation.text.trim()
                    .toString()
            ) {
                val registerRequest = RegisterRequest(
                    etName.text.toString(),
                    etEmail.text.toString(),
                    etPassword.text.toString(),
                    etDNI.text.toString()
                )
                val call = apiService.postRegister(registerRequest)
                call.enqueue(object : Callback<RegisterResponse> {
                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        toast("An error has occurred in the communication with our server. Please, try again later")
                    }

                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
                    ) {
                        if (response.isSuccessful) {
                            val registerResponse = response.body()
                            registerResponse?.let {
                                goToLoginActivity()
                            } ?: run {
                                toast("Unauthorized. Please, try again later")
                            }
                        } else {
                            toast(response.errorBody().toString())
                        }
                    }
                })
            } else {
                toast("Password and password confirmation don't match")
            }
        }
    }

    private fun goToLoginActivity() {
        clearInputs()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
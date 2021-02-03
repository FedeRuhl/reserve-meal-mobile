package com.example.reservemeal.ui.session

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.reservemeal.R
import com.example.reservemeal.io.ApiService
import com.example.reservemeal.io.response.RegisterResponse
import com.example.reservemeal.utility.toast
import kotlinx.android.synthetic.main.activity_login.*
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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnRegister.setOnClickListener {
            performRegister()
        }

    }

    private fun performRegister() {
        if (etDNI.text.isNotEmpty() && etEmail.text.isNotEmpty()
            && etName.text.isNotEmpty() && etPassword.text.isNotEmpty() && etPasswordConfirmation.text.isNotEmpty()){
                toast(etPassword.text)
                toast(etPasswordConfirmation.text)
            if (etPassword.text.trim().toString() == etPasswordConfirmation.text.trim().toString())
            {
                val call =apiService.postRegister(etName.text.toString(), etEmail.text.toString(), etPassword.text.toString(), etDNI.text.toString())
                call.enqueue(object: Callback<RegisterResponse>{
                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        toast("An error has occurred in the communication with our server. Please, try again later")
                    }

                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
                    ) {
                        if (response.isSuccessful)
                        {
                            val registerResponse = response.body()
                            registerResponse?.let{
                                goToMainActivity2()
                            } ?: run {
                                toast("Unauthorized. Please, try again later")
                            }
                        }
                        else
                        {
                            toast(response.errorBody().toString())
                        }
                    }
                })
            }
            else
            {
                toast("Password and password confirmation don't match")
            }
        }
    }

    private fun goToMainActivity2(){
        val intent = Intent(this, MainActivity2::class.java)
        startActivity(intent)
    }
}
package com.example.reservemeal.ui.session

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.reservemeal.R
import com.example.reservemeal.io.ApiService
import com.example.reservemeal.io.response.ForgetResponse
import com.example.reservemeal.utility.toast
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_forgot_password.etEmail
import kotlinx.android.synthetic.main.activity_forgot_password.etPassword
import kotlinx.android.synthetic.main.activity_forgot_password.etPasswordConfirmation
import kotlinx.android.synthetic.main.activity_forgot_password.tvGoToSignIn
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity() {
    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        btnSendVerificationCode.setOnClickListener {
            performSendEmail()
        }

        tvGoToSignIn.setOnClickListener {
            if (llStepTwo.visibility == View.VISIBLE)
            {
                llStepTwo.visibility = View.GONE
                llStepOne.visibility = View.VISIBLE
            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnChangePassword.setOnClickListener {
            performChange()

        }
    }

    private fun performChange() {
        if (etVerificationCode.text.isNotEmpty() && etPassword.text.isNotEmpty() && etPasswordConfirmation.text.isNotEmpty())
        {
            if (etPassword.text.trim().toString() == etPasswordConfirmation.text.trim().toString())
            {
                val call = apiService.postReset(etEmail.text.toString(), etPassword.text.toString(), etPasswordConfirmation.text.toString(), etVerificationCode.text.toString())
                call.enqueue(object: Callback<ForgetResponse>{
                    override fun onFailure(call: Call<ForgetResponse>, t: Throwable) {
                        toast("An error has occurred in the communication with our server. Please, try again later")
                    }

                    override fun onResponse(
                        call: Call<ForgetResponse>,
                        response: Response<ForgetResponse>
                    ) {
                        if (response.isSuccessful && response.body()?.success == true)
                        {
                            val forgetResponse = response.body()
                            forgetResponse?.let {
                                goToMainActivity2()
                            } ?: run{
                                toast("Unauthorized. Please, try again later")
                            }
                        }
                        else{
                            toast(response.body()?.message ?: response.errorBody().toString())
                        }
                    }
                })
            }
        }
    }

    private fun performSendEmail() {
        if (etEmail.text.isNotEmpty())
        {
            val call = apiService.postForget(etEmail.text.toString())
            call.enqueue(object: Callback<ForgetResponse>{
                override fun onFailure(call: Call<ForgetResponse>, t: Throwable) {
                    toast("An error has occurred in the communication with our server. Please, try again later")
                }

                override fun onResponse(
                    call: Call<ForgetResponse>,
                    response: Response<ForgetResponse>
                ) {
                    if (response.isSuccessful && response.body()?.success == true)
                    {
                        val forgetResponse = response.body()
                        forgetResponse?.let {
                            llStepOne.visibility = View.GONE
                            llStepTwo.visibility = View.VISIBLE
                        } ?: run{
                            toast("Unauthorized. Please, try again later")
                    }
                    }
                    else{
                        toast(response.body()?.message ?: response.errorBody().toString())
                    }
                }
            })
        }
    }

    private fun goToMainActivity2(){
        val intent = Intent(this, MainActivity2::class.java)
        startActivity(intent)
    }
}
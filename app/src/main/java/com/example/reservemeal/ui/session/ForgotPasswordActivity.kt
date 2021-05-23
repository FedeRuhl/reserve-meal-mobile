package com.example.reservemeal.ui.session

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.reservemeal.R
import com.example.reservemeal.io.ApiService
import com.example.reservemeal.io.response.ForgetResponse
import com.example.reservemeal.requests.ForgetRequest
import com.example.reservemeal.requests.ResetRequest
import com.example.reservemeal.utility.toast
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_forgot_password.etEmail
import kotlinx.android.synthetic.main.activity_forgot_password.etPassword
import kotlinx.android.synthetic.main.activity_forgot_password.etPasswordConfirmation
import kotlinx.android.synthetic.main.activity_forgot_password.tvGoToSignIn
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

        tvGoToSignIn.setOnClickListener {
            if (llStepTwo.visibility == View.VISIBLE) {
                hideStep(2)
            }
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnSendVerificationCode.setOnClickListener {
            performSendEmail()
        }

        btnChangePassword.setOnClickListener {
            performChange()
        }
    }

    private fun hideStep(step: Int) {
        when (step) {
            1 -> {
                llStepOne.visibility = View.GONE
                llStepTwo.visibility = View.VISIBLE
            }
            2 -> {
                llStepTwo.visibility = View.GONE
                llStepOne.visibility = View.VISIBLE
            }
        }
    }

    private fun performSendEmail() {
        if (etEmail.text.isNotEmpty()) {
            val forgetRequest = ForgetRequest(etEmail.text.toString())
            val call = apiService.postForget(forgetRequest)
            call.enqueue(object : Callback<ForgetResponse> {
                override fun onFailure(call: Call<ForgetResponse>, t: Throwable) {
                    toast("An error has occurred in the communication with our server. Please, try again later")
                }

                override fun onResponse(
                    call: Call<ForgetResponse>,
                    response: Response<ForgetResponse>
                ) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        val forgetResponse = response.body()
                        forgetResponse?.let {
                            hideStep(1)
                        } ?: run {
                            toast("Unauthorized. Please, try again later")
                        }
                    } else {
                        toast(response.body()?.message ?: response.errorBody().toString())
                    }
                }
            })
        }
    }

    private fun performChange() {
        if (etVerificationCode.text.isNotEmpty() && etPassword.text.isNotEmpty() && etPasswordConfirmation.text.isNotEmpty()) {
            if (etPassword.text.trim().toString() == etPasswordConfirmation.text.trim()
                    .toString()
            ) {
                val resetRequest = ResetRequest(
                    etEmail.text.toString(),
                    etPassword.text.toString(),
                    etPasswordConfirmation.text.toString(),
                    etVerificationCode.text.toString()
                )
                val call = apiService.postReset(resetRequest)
                call.enqueue(object : Callback<ForgetResponse> {
                    override fun onFailure(call: Call<ForgetResponse>, t: Throwable) {
                        toast("An error has occurred in the communication with our server. Please, try again later")
                    }

                    override fun onResponse(
                        call: Call<ForgetResponse>,
                        response: Response<ForgetResponse>
                    ) {
                        if (response.isSuccessful && response.body()?.success == true) {
                            hideStep(2)
                            goToLoginActivity()
                        } else {
                            toast(response.body()?.message ?: response.errorBody().toString())
                        }
                    }
                })
            } else {
                toast("Password must be the same as the password confirmation")
            }
        }
    }

    private fun clearInputs() {
        etEmail.text.clear()
        etVerificationCode.text.clear()
        etPassword.text.clear()
        etPasswordConfirmation.text.clear()
    }

    private fun goToLoginActivity() {
        clearInputs()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
package com.example.reservemeal.ui.session

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.reservemeal.R
import com.example.reservemeal.io.ApiService
import com.example.reservemeal.io.response.LoginResponse
import com.example.reservemeal.requests.LoginRequest
import com.example.reservemeal.utility.PreferenceHelper
import com.example.reservemeal.utility.PreferenceHelper.get
import com.example.reservemeal.utility.PreferenceHelper.set
import com.example.reservemeal.utility.toast
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (preferences["jwt", ""].contains("."))
            goToHomeActivity()

        tvGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        tvGoToForget.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            performLogin()
        }

    }

    private fun clearInputs() {
        etDNI.text.clear()
        etPassword.text.clear()
    }

    private fun performLogin() {
        if (etDNI.text.isNotEmpty() && etPassword.text.isNotEmpty()) {
            val loginRequest = LoginRequest(etDNI.text.toString(), etPassword.text.toString())
            val call = apiService.postLogin(loginRequest)
            call.enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    toast("An error has occurred in the communication with our server. Please, try again later")
                }

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        val loginResponse = response.body()
                        loginResponse?.let {
                            createSessionPreference(loginResponse.jwt)
                            goToHomeActivity()
                        } ?: run {
                            toast("Unauthorized. Please, try again later")
                        }
                    } else {
                        toast("The credentials don't match")
                    }
                }
            })
        }
    }

    private fun createSessionPreference(accessToken: String) {
        preferences.all.clear()
        preferences["jwt"] = accessToken
    }

    private fun goToHomeActivity() {
        clearInputs()

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}
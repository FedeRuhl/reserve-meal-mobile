package com.example.reservemeal.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.reservemeal.R
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        btnSendVerificationCode.setOnClickListener {
            llStepOne.visibility = View.GONE
            llStepTwo.visibility = View.VISIBLE
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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
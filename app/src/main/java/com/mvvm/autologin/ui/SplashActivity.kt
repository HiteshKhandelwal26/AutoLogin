package com.mvvm.autologin.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.mvvm.autologin.data.SessionManager
import com.mvvm.autologin.databinding.ActivitySplashBinding
import com.mvvm.autologin.ui.presentation.dashboard.MainActivity
import com.mvvm.autologin.ui.presentation.login.LoginActivity


class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler().postDelayed({
            val token = SessionManager.getToken(this)
            if (!token.isNullOrBlank()) {
                navigateToHome()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, 1000)
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
        finish()
    }
}
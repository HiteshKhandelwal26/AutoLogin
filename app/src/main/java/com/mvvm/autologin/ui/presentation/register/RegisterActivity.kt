package com.mvvm.autologin.ui.presentation.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mvvm.postquery.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onCreate() {
        finish()
    }
}
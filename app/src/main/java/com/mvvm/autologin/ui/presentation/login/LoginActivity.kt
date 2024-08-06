package com.mvvm.autologin.ui.presentation.login

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.mvvm.autologin.Utils
import com.mvvm.autologin.data.SessionManager
import com.mvvm.autologin.data.model.BaseResponse
import com.mvvm.autologin.data.model.LoginDummyResponse
import com.mvvm.autologin.ui.presentation.dashboard.MainActivity
import com.mvvm.autologin.ui.presentation.register.RegisterActivity
import com.mvvm.postquery.R
import com.mvvm.postquery.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun login() {
        showToast(
            Utils.validateEmailPassword(
                binding.email.text.toString(),
                binding.password.text.toString(),
                this
            )
        )
        if (Utils.isAllValidated()) {
            if (Utils.isInternetAvailable(this)) {
                showLoading()
                doLogin()
            } else {
                showToast(getString(R.string.no_internet))
            }
        }
    }

    private fun observerData() {
        //TODO temporary commenting the actual call and loading the dummy response
        /*loginViewModel.loginResult.observe(this) {*/
        //TODO calling the dummy response for testing
        loginViewModel.dummyResult.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()
                    processLogin(it.data)
                }

                is BaseResponse.Error -> {
                    stopLoading()
                    processError(it.msg)
                }

                else -> {
                    stopLoading()
                }
            }
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }


    private fun doLogin() {
        val email = binding.email.text.toString()
        val pwd = binding.password.text.toString()
        loginViewModel.loginUser(email = email, pwd = pwd)
        observerData()
    }

    fun createAccountOnClick() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun processLogin(data: LoginDummyResponse?) {
        showToast("Success:" + data?.status)
        if (!data?.data?.token.isNullOrEmpty()) {
            data?.data?.token?.let { SessionManager.saveAuthToken(this, it) }
            navigateToHome()
        }
    }

    private fun processError(msg: String?) {
        showToast("" + msg)
    }

    private fun showToast(msg: String) {
        val snackbar = Snackbar.make(binding.loginLayout, msg, Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}
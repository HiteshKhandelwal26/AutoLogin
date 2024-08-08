package com.mvvm.autologin.ui.presentation.login

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import com.mvvm.autologin.ui.utils.Utils
import com.mvvm.autologin.data.SessionManager
import com.mvvm.autologin.data.model.BaseResponse
import com.mvvm.autologin.data.model.LoginDummyResponse
import com.mvvm.autologin.ui.utils.hideProgressBar
import com.mvvm.autologin.ui.utils.showProgressBar
import com.mvvm.autologin.ui.utils.showSnackbar
import com.mvvm.autologin.ui.presentation.home.MainActivity
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

    fun login(v: View) {
        binding.loginLayout.showSnackbar(
            Utils.validateEmailPassword(
                binding.email.text.toString(),
                binding.password.text.toString(),
                this
            )
        )
        if (Utils.isAllValidated()) {
            if (Utils.isInternetAvailable(this)) {
                binding.progressBar.showProgressBar()
                doLogin()
            } else {
                binding.loginLayout.showSnackbar(getString(R.string.no_internet))
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
                    binding.progressBar.showProgressBar()
                }

                is BaseResponse.Success -> {
                    binding.progressBar.hideProgressBar()
                    processLogin(it.mResult)
                }

                is BaseResponse.Error -> {
                    binding.progressBar.hideProgressBar()
                    processError(it.msg)
                }

                else -> {
                    binding.progressBar.hideProgressBar()
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
        //TODO uncomment the below line when calling the actual login api, and comment the dummy response call
        /*val email = binding.email.text.toString()
        val pwd = binding.password.text.toString()
        loginViewModel.loginUser(email = email, pwd = pwd)*/
        loginViewModel.loginWithDummyResponse()
        observerData()
    }

    fun createAccountOnClick(v:View) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun processLogin(data: LoginDummyResponse?) {
        binding.loginLayout.showSnackbar("Success:" + data?.status)
        if (!data?.data?.token.isNullOrEmpty()) {
            data?.data?.token?.let { SessionManager.saveAuthToken(this, it) }
            navigateToHome()
        }
    }

    private fun processError(msg: String?) {
        binding.loginLayout.showSnackbar("" + msg)
    }
}
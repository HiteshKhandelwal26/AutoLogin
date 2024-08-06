package com.mvvm.autologin.ui.presentation.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.mvvm.autologin.data.repository.LoginRepository
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mvvm.autologin.data.model.BaseResponse
import com.mvvm.autologin.data.model.Data
import com.mvvm.autologin.data.model.LoginDummyResponse
import com.mvvm.autologin.data.model.LoginRequest
import com.mvvm.autologin.data.model.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(application: Application, private val repository: LoginRepository) :
    AndroidViewModel(application) {

    /*Main Coding*/
    val loginResult: MutableLiveData<BaseResponse<LoginResponse>> = MutableLiveData()

    val dummyResult: MutableLiveData<BaseResponse<LoginDummyResponse>> = MutableLiveData()


    fun loginUser(email: String, pwd: String) {
        loginResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                    val loginRequest = LoginRequest(
                        password = pwd,
                        email = email
                    )
                    //TODO - temporary commenting the below condition as the API is not having any response.
                    //TODO - Therefore loading the dummy response for time being for Login FLOW
                    /*
                    val response = repository.loginRequest(loginRequest = loginRequest)
                    if (response.code() == 200) {
                        loginResult.value = BaseResponse.Success(response.body())
                    } else {
                        loginResult.value = BaseResponse.Error(response.message())
                    }*/
                    loadDummyResponse()

            } catch (ex: Exception) {
                loginResult.value = BaseResponse.Error(ex.message)
            }
        }
    }

    private fun loadDummyResponse() {
        viewModelScope.launch {
            dummyResult.value = BaseResponse.Loading()
            val response = readJsonFile("login_response.json")
            response.let {
                val jsonObject = JSONObject(it)
                val isSuccess = jsonObject.getString("status")
                val code = jsonObject.getInt("code")
                if (isSuccess == "success") {
                    val dataDummy = jsonObject.getJSONObject("data")
                    val user = Data(
                        name = dataDummy.getString("name"),
                        id = dataDummy.getInt("id"),
                        email = dataDummy.getString("email"),
                        token = dataDummy.getString("token")
                    )
                    dummyResult.value =
                        BaseResponse.Success(data = LoginDummyResponse(code, user, isSuccess))
                } else {
                    BaseResponse.Error("Error in loading asset file")
                }
            }
        }
    }

    private suspend fun readJsonFile(filename: String): String {
        return withContext(Dispatchers.IO) {
            val assetManager = getApplication<Application>().assets
            val inputStream = assetManager.open(filename)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            bufferedReader.forEachLine { stringBuilder.append(it) }
            bufferedReader.close()
            inputStream.close()
            stringBuilder.toString()
        }
    }

    /* fun loginButton() {
         if (inputUsername.value == null || inputPassword.value == null) {
             _errorToast.value = true
         } else {
             uiScope.launch {
                 val usersNames = repository.loginRequest(inputUsername.value!!)
                 if (usersNames != null) {
                     if (usersNames.passwrd == inputPassword.value) {
                         inputUsername.value = null
                         inputPassword.value = null
                         _navigatetoUserDetails.value = true
                     } else {
                         _errorToastInvalidPassword.value = true
                     }
                 } else {
                     _errorToastUsername.value = true
                 }
             }
         }
     }*/
}
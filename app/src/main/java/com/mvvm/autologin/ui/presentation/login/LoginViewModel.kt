package com.mvvm.autologin.ui.presentation.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.mvvm.autologin.data.repository.APIRepository
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mvvm.autologin.data.model.BaseResponse
import com.mvvm.autologin.data.model.Data
import com.mvvm.autologin.data.model.LoginDummyResponse
import com.mvvm.autologin.ui.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val repository: APIRepository
) :
    AndroidViewModel(application) {

    /* //TODO to be un-commented when the actual login api is called
    val loginResult: MutableLiveData<BaseResponse<LoginResponse>> = MutableLiveData()*/
    val dummyResult: MutableLiveData<BaseResponse<LoginDummyResponse>> = MutableLiveData()

    //TODO the api is not returning any data, therefore making the fake api call below and commenting this block
    /*fun loginUser(email: String, pwd: String) {
            loginResult.value = BaseResponse.Loading()
            viewModelScope.launch {
                try {
                    val loginRequest = LoginRequest(
                        password = pwd,
                        email = email
                    )
                    val response = repository.loginRequest(loginRequest = loginRequest)
                    if (response.code() == 200) {
                        loginResult.value = BaseResponse.Success(response.body())
                    } else {
                        loginResult.value = BaseResponse.Error(response.message())
                    }
                } catch (ex: Exception) {
                    loginResult.value = BaseResponse.Error(ex.message)
                }
            }
        }*/

    fun loginWithDummyResponse() {
        viewModelScope.launch {
            try {
                loadDummyResponse()
            } catch (ex: Exception) {
                dummyResult.value = BaseResponse.Error(ex.message)
            }
        }
    }

    private fun loadDummyResponse() {
        viewModelScope.launch {
            dummyResult.value = BaseResponse.Loading()
            val response = readJsonFile("login_response.json")
            response.let {
                val jsonObject = JSONObject(it)
                val isSuccess = jsonObject.getString(Utils.status)
                val code = jsonObject.getInt(Utils.code)
                if (isSuccess == Utils.success) {
                    val dataDummy = jsonObject.getJSONObject(Utils.data)
                    val user = Data(
                        name = dataDummy.getString(Utils.name),
                        id = dataDummy.getInt(Utils.id),
                        email = dataDummy.getString(Utils.email),
                        token = dataDummy.getString(Utils.token)
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
}
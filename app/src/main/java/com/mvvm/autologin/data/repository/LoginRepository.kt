package com.mvvm.autologin.data.repository

import com.mvvm.autologin.data.datasource.APIService
import com.mvvm.autologin.data.model.LoginRequest
import com.mvvm.autologin.data.model.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository @Inject constructor(val dataSource: APIService) {

    suspend fun loginRequest(loginRequest: LoginRequest) =
        dataSource.loginUser(loginRequest = loginRequest)

    /*API call using FLow Builder*/
    suspend fun makeLoginRequest(loginRequest: LoginRequest): Flow<Response<LoginResponse>> {
        return flow {
            emit(dataSource.loginUser(loginRequest))
        }
    }
}

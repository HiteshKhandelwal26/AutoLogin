package com.mvvm.autologin.data.repository

import com.mvvm.autologin.data.datasource.APIService
import com.mvvm.autologin.data.model.DashboardResponse
import com.mvvm.autologin.data.model.LoginRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class APIRepository @Inject constructor(val dataSource: APIService) {

    suspend fun loginRequest(loginRequest: LoginRequest) =
        dataSource.loginUser(loginRequest = loginRequest)

    /*API call using FLow Builder*/
    suspend fun dashboardRequest(): Flow<DashboardResponse> {
        return flow {
            emit(dataSource.getListData())
        }
    }
}

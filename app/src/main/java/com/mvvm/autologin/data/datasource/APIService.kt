package com.mvvm.autologin.data.datasource

import com.mvvm.autologin.data.model.DashboardResponse
import com.mvvm.autologin.data.model.LoginRequest
import com.mvvm.autologin.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIService {
    @POST("users")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("api/users")
    suspend fun getListData(): DashboardResponse
}
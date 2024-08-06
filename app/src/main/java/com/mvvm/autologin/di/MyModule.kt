package com.mvvm.autologin.di

import com.mvvm.autologin.data.datasource.APIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

//const val BASE_URL = "https://jsonplaceholder.typicode.com"
const val BASE_URL = "http://restapi.adequateshop.com"

@Module
@InstallIn(SingletonComponent::class)
class MyModule {

    private val httpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun getOkhttp(): OkHttpClient {
        return OkHttpClient().newBuilder().addInterceptor(httpLoggingInterceptor).build()
    }

    @Provides
    @Singleton
    fun getRetrofitInstance(okHttpClient: OkHttpClient): APIService {
        val apiService: APIService by lazy {
            Retrofit.Builder().client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIService::class.java)
        }
        return apiService
    }
}
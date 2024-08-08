package com.mvvm.autologin.data.model

sealed class BaseResponse<out T> {
    data class Success<out T>(val mResult: T? = null) : BaseResponse<T>()
    data class Loading(val nothing: Nothing?=null) : BaseResponse<Nothing>()
    data class Error(val msg: String?) : BaseResponse<Nothing>()
}
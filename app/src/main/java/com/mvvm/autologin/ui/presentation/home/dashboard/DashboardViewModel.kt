package com.mvvm.autologin.ui.presentation.home.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvvm.autologin.data.model.BaseResponse
import com.mvvm.autologin.data.model.DashboardResponse
import com.mvvm.autologin.data.repository.APIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val repository: APIRepository) : ViewModel() {
    private val _getListData =
        MutableStateFlow<BaseResponse<DashboardResponse>>(BaseResponse.Loading())
    val getDashboardList = _getListData

    fun fetchDashboardList() {
        viewModelScope.launch {
            _getListData.value = BaseResponse.Loading()
            repository.dashboardRequest()
                .catch {
                    _getListData.value = BaseResponse.Error(it.message)
                }.retryWhen { cause, attempt ->
                    return@retryWhen cause is IOException && attempt < 3
                }.collect {
                    _getListData.value = BaseResponse.Success(it)
                }
        }
    }
}
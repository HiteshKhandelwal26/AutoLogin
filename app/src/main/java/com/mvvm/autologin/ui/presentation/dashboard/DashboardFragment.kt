package com.mvvm.autologin.ui.presentation.dashboard

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mvvm.autologin.ui.utils.Utils
import com.mvvm.autologin.data.SessionManager
import com.mvvm.autologin.data.model.BaseResponse
import com.mvvm.autologin.ui.utils.hideProgressBar
import com.mvvm.autologin.ui.utils.showProgressBar
import com.mvvm.autologin.ui.utils.showSnackbar
import com.mvvm.postquery.R
import com.mvvm.postquery.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch


class DashboardFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val dashboardViewModel: DashboardViewModel by viewModels()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Initialize MyObject with context
        SessionManager.init(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callDashboardAPI()
    }

    private fun callDashboardAPI() {
        if (Utils.isInternetAvailable(requireContext())) {
            dashboardViewModel.fetchDashboardList()
            observeData()
        } else {
            binding.dashboardLayout.showSnackbar(getString(R.string.no_internet))
        }
    }

    private fun observeData() {
        binding.progressBar.showProgressBar()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                dashboardViewModel.getDashboardList.collect {
                    when (it) {
                        is BaseResponse.Loading -> {
                            binding.progressBar.showProgressBar()
                        }
                        is BaseResponse.Success -> {
                            binding.progressBar.hideProgressBar()
                            //TODO https://outcomeschool.com/blog/mvvm-architecture-android
                        }
                        is BaseResponse.Error -> {
                            binding.progressBar.hideProgressBar()
                            binding.dashboardLayout.showSnackbar(it.msg ?: "Error")
                        }


                    }
                }

            }
        }
    }
}
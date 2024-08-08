package com.mvvm.autologin.ui.presentation.home.dashboard

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mvvm.autologin.ui.utils.Utils
import com.mvvm.autologin.data.SessionManager
import com.mvvm.autologin.data.model.BaseResponse
import com.mvvm.autologin.data.model.DataItem
import com.mvvm.autologin.ui.utils.hideProgressBar
import com.mvvm.autologin.ui.utils.showProgressBar
import com.mvvm.autologin.ui.utils.showSnackbar
import com.mvvm.postquery.R
import com.mvvm.postquery.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class DashboardFragment : Fragment(), onAdapterItemClicked {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val dashboardViewModel: DashboardViewModel by activityViewModels()
    private lateinit var listAdapter: DashboardListAdapter
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
            prepareRecyclerView()
            dashboardViewModel.fetchDashboardList()
            observeData()
        } else {
            hideListView()
            binding.progressBar.hideProgressBar()
            binding.dashboardLayout.showSnackbar(getString(R.string.no_internet))
        }
    }

    private fun prepareRecyclerView() {
        listAdapter = DashboardListAdapter(this)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter
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
                            showListView()
                            listAdapter.setMyList(it.mResult?.data)
                        }

                        is BaseResponse.Error -> {
                            hideListView()
                            binding.progressBar.hideProgressBar()
                            binding.dashboardLayout.showSnackbar(it.msg ?: "Error")
                        }
                    }
                }

            }
        }
    }

    override fun onitemClicked(mDataList: DataItem) {
        binding.dashboardLayout.showSnackbar(getString(R.string.wip) + " For item no: " + mDataList.id)
    }

    private fun showListView() {
        binding.recyclerView.visibility = View.VISIBLE
        binding.txtNoData.visibility = View.GONE
    }

    private fun hideListView() {
        binding.recyclerView.visibility = View.GONE
        binding.txtNoData.visibility = View.VISIBLE
    }
}
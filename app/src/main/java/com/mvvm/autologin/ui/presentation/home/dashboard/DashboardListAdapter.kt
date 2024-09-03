package com.mvvm.autologin.ui.presentation.home.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mvvm.autologin.data.model.DataItem
import com.mvvm.autologin.databinding.ItemLayoutDashboardBinding

class DashboardListAdapter(onItemTapped: onAdapterItemClicked) :
    RecyclerView.Adapter<DashboardListAdapter.ViewHolder>() {
    private var dataList = ArrayList<DataItem>()
    private val onItemTapp: onAdapterItemClicked = onItemTapped
    fun setMyList(data: List<DataItem?>?) {
        this.dataList = data as ArrayList<DataItem>
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemLayoutDashboardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLayoutDashboardBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(dataList[position].avatar)
            .into(holder.binding.imageViewBanner)

        holder.binding.tvFName.text = dataList[position].firstName
        holder.binding.tvLName.text = dataList[position].lastName
        holder.binding.tvEmail.text = dataList[position].email
        holder.binding.adapterLayout.setOnClickListener{
            onItemTapp.onitemClicked(dataList[position])
        }
    }
}

interface onAdapterItemClicked {
    fun onitemClicked(mDataList: DataItem)
}
//https://www.geeksforgeeks.org/android-build-a-movie-app-using-retrofit-and-mvvm-architecture-with-kotlin/
package com.mvvm.autologin.ui.presentation.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mvvm.postquery.R
import com.mvvm.postquery.databinding.ActivityMyMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(DashboardFragment())
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(DashboardFragment())
                    true
                }

                R.id.admin -> {
                    loadFragment(AdminFragment())
                    true
                }

                R.id.settings -> {
                    loadFragment(SettingFragment())
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }
}
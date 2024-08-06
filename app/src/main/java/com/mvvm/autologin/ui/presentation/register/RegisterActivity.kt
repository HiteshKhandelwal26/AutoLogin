package com.mvvm.autologin.ui.presentation.register

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvvm.autologin.databinding.ActivityRegisterBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* binding.register.setOnClickListener {
             doRegister()
         }*/

    }

    fun onCreate(view: View) {
        finish()
    }

}

class A(val repo: Repo) : ViewModel() {

    fun load() {
        viewModelScope.launch {
            repo.lList()
                .flowOn(Dispatchers.IO)
                .retryWhen { cause, attempt ->
                    return@retryWhen cause is IndexOutOfBoundsException && attempt < 2
                }
                .collect {
                    it
                }
        }
    }

}

interface APIs {
    suspend fun getData(): kotlinx.coroutines.flow.Flow<List<String>>
}

class Repo(val apIs: APIs) {
    suspend fun lList() = flow<List<String>> {
        emit(mutableListOf(apIs.getData().toString()))
    }


    fun loader(): kotlinx.coroutines.flow.Flow<String> {
        return flow<String> {
            emit("")
        }
    }

    suspend fun loadd() {
        val col = loader()
        col.collect {
            it
        }
    }

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
    annotation class MyClass
    {

    }

    @MyClass()
    fun lad()
    {

    }
}
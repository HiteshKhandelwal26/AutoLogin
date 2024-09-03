package com.mvvm.autologin.data

import android.content.Context
import android.content.SharedPreferences
import com.mvvm.autologin.R

object SessionManager {
    private var appContext: Context? = null
    const val USER_TOKEN = "user_token"

    fun init(context: Context) {
        // Use applicationContext to avoid memory leaks
        this.appContext = context.applicationContext
    }

    /**
     * Function to save auth token
     */
    fun saveAuthToken(context: Context, token: String) {
        saveString(context, USER_TOKEN, token)
    }

    /**
     * Function to fetch auth token
     */
    fun getToken(context: Context): String? {
        return getString(context, USER_TOKEN)
    }

    fun saveString(context: Context, key: String, value: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()

    }

    fun getString(context: Context, key: String): String? {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        return prefs.getString(this.USER_TOKEN, null)
    }

    fun clearData() {
        appContext?.let {
            it.getSharedPreferences(it.getString(R.string.app_name), Context.MODE_PRIVATE)
                .edit()
                .clear().apply()
        }
    }
}
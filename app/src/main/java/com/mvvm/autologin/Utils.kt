package com.mvvm.autologin

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Patterns
import androidx.fragment.app.FragmentActivity
import com.mvvm.autologin.data.SessionManager
import com.mvvm.postquery.R

object Utils {

    private lateinit var myValue: String
    fun validateEmailPassword(email: String, password: String): String {
        myValue = if (email.isEmpty() && password.isEmpty()) {
            "Enter email and password"
        } else if (email.isEmpty()) {
            "Enter email "

        } else if (password.isEmpty()) {
            "Enter password "
        } else if (!isUserNameValid(email)) {
            "Enter valid email"
        } else if (!isPasswordValid(password)) {
            "Password should be more than 5"
        } else {
            ""
        }
        return myValue
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    fun isAllValidated(): Boolean {
        return myValue.isEmpty()
    }

    /*Logout Dialog*/
    fun showAlertDialog(activity: FragmentActivity?) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.app_name)
        builder.setMessage(R.string.logout_msg)

        builder.setPositiveButton(R.string.logout_yes) { _, _ ->
            SessionManager.clearData()
            activity?.finish()
        }
        builder.setNegativeButton(R.string.logout_no) { dialog, _ ->
            dialog.dismiss()
        }
        // Create and show the dialog
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /*Check network availability*/
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}
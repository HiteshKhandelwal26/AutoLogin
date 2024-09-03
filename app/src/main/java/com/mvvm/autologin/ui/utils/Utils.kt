package com.mvvm.autologin.ui.utils

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Patterns
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import com.mvvm.autologin.data.SessionManager
import com.mvvm.autologin.R

object Utils {

    const val name: String = "name"
    const val status: String = "status"
    const val code: String = "code"
    const val success: String = "success"
    const val id: String = "id"
    const val email: String = "email"
    const val token: String = "token"
    const val data: String = "data"

    private lateinit var myValue: String
    fun validateEmailPassword(email: String, password: String, context: Context): String {
        myValue = if (email.isEmpty() && password.isEmpty()) {
            context.getString(R.string.enter_email_pwd)
        } else if (email.isEmpty()) {
            context.getString(R.string.enter_email)
        } else if (password.isEmpty()) {
            context.getString(R.string.enter_pwd)
        } else if (!isValidEmail(email)) {
            context.getString(R.string.enter_valid_email)
        } else if (!isPasswordValid(password)) {
            context.getString(R.string.enter_valid_password)
        } else {
            ""
        }
        return myValue
    }

    // A placeholder username validation check
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
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

/*Extension Functions*/
fun View.showSnackbar(message: String, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, message, length).show()
}

fun View.showProgressBar() {
    this.visibility = View.VISIBLE
}

fun View.hideProgressBar() {
    this.visibility = View.GONE
}
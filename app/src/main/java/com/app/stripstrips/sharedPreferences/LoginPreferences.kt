package com.app.stripstrips.sharedPreferences
import android.content.Context
import android.content.SharedPreferences

class LoginPreferences {

    val PREF_NAME = "Login_PREF"
    val MODE = Context.MODE_PRIVATE


    fun writeString(
        context: Context,
        key: String?,
        value: String?
    ) {
        getEditor(context).putString(key, value).apply()
    }

    fun readString(
        context: Context,
        key: String?,
        defValue: String?
    ): String? {
        return getPreferences(context).getString(key, defValue)
    }

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, MODE)
    }

    private fun getEditor(context: Context): SharedPreferences.Editor {
        return getPreferences(context).edit()
    }
}
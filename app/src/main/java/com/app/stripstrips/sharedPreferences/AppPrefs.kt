package com.app.stripstrips.sharedPreferences
import android.content.Context
import android.content.SharedPreferences


class AppPrefs (private val ctx: Context){

     private fun getPrefs(): SharedPreferences {
        return ctx.getSharedPreferences("STRIPS&TRIPS", Context.MODE_PRIVATE)
    }

     fun clearData(){
         val edit = getPrefs().edit()
         edit.clear()
         edit.apply()
     }

    fun setString(key: String, value:String) {
        val edit = getPrefs().edit()
        edit.putString(key, value)
        edit.apply()
    }
    fun getString(key: String) : String?{
        return getPrefs().getString(key,"")
    }
    fun setBoolean(key: String, value: Boolean){
        val edit = getPrefs().edit()
        edit.putBoolean(key, value)
        edit.apply()
    }

    fun getBoolean(key: String) :Boolean{
        return getPrefs().getBoolean(key,false)
    }

    fun setNameString(key: String, value:String) {
        val edit = getPrefs().edit()
        edit.putString(key, value)
        edit.apply()
    }
    fun getNameStringKey(key: String) :String?{
        return getPrefs().getString(key,"")
    }

    fun setToken(key : String,value: String){
        val edit = getPrefs().edit()
        edit.putString(key, value)
        edit.apply()
    }
    fun getToken(key: String) :String?{
        return getPrefs().getString(key,"")
    }
    fun setUserId(key: String,value: String) {
        val edit = getPrefs().edit()
        edit.putString(key, value)
        edit.apply()
    }
    fun getUserId(key: String):String? {
        return getPrefs().getString(key,"")
    }

    fun setLoggedIn(key: String,value: String) {
        val edit = getPrefs().edit()
        edit.putString(key, value)
        edit.apply()
    }

    fun getLoggedIn(key: String):String? {
        return getPrefs().getString(key,"")
    }
}
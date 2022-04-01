package com.kvr.user.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.DisplayMetrics
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.res.ResourcesCompat
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kvr.user.BaseApplication
import com.kvr.user.MainActivity
import com.kvr.user.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.util.*


object Helpers {
    fun showToast(context: Activity?, type:Int = 0, message:String = "") {
        if(context != null){
            when(type){
                0 ->  MotionToast.createColorToast(context,"Success", message,
                    MotionToastStyle.SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(context, R.font.roboto))
                1 -> MotionToast.createColorToast(context,"Error", message,
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(context, R.font.roboto))
                2 -> MotionToast.createColorToast(context,"Alert", message,
                    MotionToastStyle.INFO,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(context, R.font.roboto))
                3 -> MotionToast.createColorToast(context,"Internet", "No internet available",
                    MotionToastStyle.NO_INTERNET,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(context, R.font.roboto))
                4 -> MotionToast.createColorToast(context,"Delete", message,
                    MotionToastStyle.DELETE,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(context, R.font.roboto))
            }
        }

    }

    fun networkCheck(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    fun getGSON() : Gson {
        val gb = GsonBuilder()
        gb.registerTypeAdapter(String::class.java, StringConverter())
        return gb.create()
    }

    fun removeNull(value:String? = null) : String{
        if(value == null || value.isEmpty() || value.trim() == "null") return ""
        return value
    }

    fun stringToStringArray(value:String? = null) : Array<String>{
        if(value == null || value.isEmpty() || value.trim() == "null") return arrayOf<String>()
        return value.replace("[\\[\"]".toRegex(),"").replace("]","").split(",").toTypedArray()
    }

    fun stringToJSONString(value:String? = null) : String{
        if(value == null || value.isEmpty() || value.trim() == "null") return ""
        return value.replace("\\\\".toRegex(),"")
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun setLocale(context: Activity?, lang: String = "en", recreate: Boolean = true) {
        if(context == null) return
        val langLag = Locale(lang)
        val res: Resources = context.getResources()
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.setLocale(langLag)
        res.updateConfiguration(conf, dm)
        GlobalScope.async(Dispatchers.IO) {
            BaseApplication.appContext.appPref.putString(Constants.PREF_LANG, lang)
        }
        if(recreate){
            context.recreate()
        }
    }

}
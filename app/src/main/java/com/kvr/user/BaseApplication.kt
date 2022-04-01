package com.kvr.user

import android.app.Application
import android.util.Log
import androidx.constraintlayout.solver.widgets.Helper
import androidx.lifecycle.lifecycleScope
import com.facebook.stetho.Stetho
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.interceptors.loggingResponseInterceptor
import com.kvr.user.utils.AppPref
import com.kvr.user.utils.Constants
import com.kvr.user.utils.Helpers
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch

class BaseApplication: Application() {

    companion object {
        lateinit var appContext: BaseApplication
        var isNetAvailable  = true
    }

    lateinit var appPref: AppPref

    override fun onCreate() {
        super.onCreate()
        appContext = this
        appPref = AppPref(context = this)
        Stetho.initializeWithDefaults(this)
        isNetAvailable= Helpers.networkCheck(this)
    }
}
package com.kvr.user

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.interceptors.loggingResponseInterceptor
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.kvr.user.model.PaymentListerner
import com.kvr.user.screen.MainScreen
import com.kvr.user.ui.theme.AppUserTheme
import com.kvr.user.utils.Constants
import com.kvr.user.utils.Helpers
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity(), PaymentResultWithDataListener {

    lateinit var paymentListerner: PaymentListerner

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent?.action == "FirebaseToken"){
                lifecycleScope.launch {
                    intent.getStringExtra("token")?.let {
                        Log.e("broadcastReceiver", it)
                        BaseApplication.appContext.appPref.putString(
                            Constants.PREF_DEVICE_TOKEN,
                            it
                        )
                    }
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        val manager = FuelManager.instance.apply {
            basePath = Constants.BASE_URL
            addResponseInterceptor { loggingResponseInterceptor() }
        }
        val appPref = BaseApplication.appContext.appPref
        lifecycleScope.launch {
            val token = appPref.getString(Constants.ACCESS_TOKEN)
            if(token?.isNotEmpty() == true){
                Log.e("MainActivity", token)
                manager.baseHeaders =  mapOf("Authorization" to "Bearer ${token}")
            }
            val lang = appPref.getString(Constants.PREF_LANG)
            if(lang?.isNotEmpty() == true)  Helpers.setLocale(this@MainActivity, lang,false)
            else Helpers.setLocale(this@MainActivity, "en",false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppUserTheme() {
                MainScreen()
            }
        }
        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this@MainActivity,permissions,1500)
        //razorPay
        Checkout.preload(BaseApplication.appContext)

        //token
        FirebaseMessaging.getInstance().token.addOnCompleteListener(this@MainActivity,object : OnCompleteListener<String>{
            override fun onComplete(task: Task<String>) {
                if (!task.isSuccessful) {
                    Log.e("FirebaseMessaging", "getInstanceId failed", task.exception)
                    return;
                }
                if(task.isSuccessful && task.result.isNotEmpty()){
                    Log.e("FirebaseMessaging", task.result)
                    lifecycleScope.launch {
                        BaseApplication.appContext.appPref.putString(
                            Constants.PREF_DEVICE_TOKEN,
                            task.result
                        )
                    }
                }
            }
        })

    }

    override fun onPaymentSuccess(razorpayPaymentId: String?, paymentData: PaymentData?) {
        paymentListerner.success(razorpayPaymentId, paymentData)
    }

    override fun onPaymentError(errorCode: Int, razorpayPaymentId: String?, paymentData: PaymentData?) {
        paymentListerner.failure(errorCode,razorpayPaymentId, paymentData)
        //Log.e("[Payment errorCode]", "${errorCode}")
    }

    override fun onResume() {
        super.onResume()
        val filterIntent = IntentFilter()
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,filterIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }
}



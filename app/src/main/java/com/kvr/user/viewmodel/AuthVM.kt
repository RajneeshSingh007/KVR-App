package com.kvr.user.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.FuelManager
import com.kvr.user.BaseApplication
import com.kvr.user.model.*
import com.kvr.user.network.ApiServices
import com.kvr.user.network.Response
import com.kvr.user.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthVM : ViewModel() {
    private var _state = MutableStateFlow<Response<Login>>(Response.Empty())
    val state = _state.asStateFlow()

    private var regstate = MutableStateFlow<Response<Register>>(Response.Empty())
    val registeState = regstate.asStateFlow()

    private var otpstate = MutableStateFlow<Response<Otp>>(Response.Empty())
    val _otpState = otpstate.asStateFlow()

    fun loginApiCall(loginReq: LoginReq) = viewModelScope.launch {
        _state.value = Response.Loading(true)
        val appPref = BaseApplication.appContext.appPref
        loginReq.device_token = appPref.getString(Constants.PREF_DEVICE_TOKEN).toString()
        try{
            _state.value = ApiServices.loginApi(loginReq = loginReq)
            if(_state.value.data?.status == true){
                val loginData = _state.value.data?.data as LoginData
                appPref.putString(Constants.USER_ID, loginData.id.toString())
                appPref.putString(Constants.ROLE_ID, loginData.role_id.toString())
                appPref.putString(Constants.ACCESS_TOKEN, loginData.token.toString())
                appPref.putString(Constants.IS_LOGGEDIN, "true")
            }else{
                _state.value.data?.message?.let { _state.value = Response.Error(it) }
            }
            delay(16)
            _state.value = Response.Loading(false)
        }catch (e:FuelError){
            e.message?.let { _state.value =  Response.Error(it) }
            delay(16)
            _state.value = Response.Loading(false)
        }
    }

    fun registerApiCall(registerReq: RegisterReq) = viewModelScope.launch {
        otpstate.value = Response.Loading(true)
        try{
            otpstate.value = ApiServices.registerApi(registerReq = registerReq)
            val errorMsg = if(otpstate.value.data?.status == false){
                otpstate.value.data?.message
            }else{
                ""
            }
            delay(16)
            otpstate.value = Response.Loading(false)
            delay(16)
            if(errorMsg?.isNotEmpty() == true){
                otpstate.value =  Response.Error(errorMsg.toString())
            }
        }catch (e:FuelError){
            e.message?.let { otpstate.value =  Response.Error(it) }
            delay(16)
            otpstate.value = Response.Loading(false)
        }
    }

    fun verifyOtpApiCall(otpReq: OtpReq) = viewModelScope.launch {
        regstate.value = Response.Loading(true)
        try{
            regstate.value = ApiServices.verifyOtp(otpReq)
            val appPref = BaseApplication.appContext.appPref
            if(regstate.value.data?.status == true){
                val regData = regstate.value.data?.data as RegisterData
                appPref.putString(Constants.USER_ID, regData.id.toString())
                appPref.putString(Constants.ACCESS_TOKEN, regData.token.toString())
                appPref.putString(Constants.IS_LOGGEDIN, "true")
            }else{
                regstate.value.data?.message?.let { regstate.value = Response.Error(it) }
            }
            delay(16)
            regstate.value = Response.Loading(false)
        }catch (e:FuelError){
            e.message?.let { regstate.value =  Response.Error(it) }
            delay(16)
            regstate.value = Response.Loading(false)
        }
    }

    fun reSendOtpCall(otpReq: OtpReq) = viewModelScope.launch {
        otpstate.value = Response.Loading(true)
        try{
            otpstate.value = ApiServices.resendOtpApi(otpReq)
            val errorMsg = if(otpstate.value.data?.status == false){
                otpstate.value.data?.message
            }else{
                ""
            }
            delay(16)
            otpstate.value = Response.Loading(false)
            delay(16)
            if(errorMsg?.isNotEmpty() == true){
                otpstate.value =  Response.Error(errorMsg.toString())
            }
        }catch (e:FuelError){
            e.message?.let { otpstate.value =  Response.Error(it) }
            delay(16)
            otpstate.value = Response.Loading(false)
        }
    }
}
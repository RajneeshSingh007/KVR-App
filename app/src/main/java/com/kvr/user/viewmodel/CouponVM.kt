package com.kvr.user.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.fuel.core.FuelError
import com.kvr.user.model.*
import com.kvr.user.network.ApiServices
import com.kvr.user.network.Response
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CouponVM : ViewModel() {
    private var _state = MutableStateFlow<Response<Coupons>>(Response.Empty())
    val state = _state.asStateFlow()

    fun fetchCoupons() = viewModelScope.launch {
        _state.value = Response.Loading(true)
        try{
            val result  = ApiServices.getCouponsApi()
            if(result.data?.status == true){
                _state.value = result
            }
        }catch (e: FuelError){
            e.message?.let { _state.value =  Response.Error(it) }
            delay(16)
            _state.value = Response.Loading(false)
        }finally {
            delay(16)
            _state.value = Response.Loading(false)
        }
    }
}
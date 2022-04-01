package com.kvr.user.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.fuel.core.FuelError
import com.kvr.user.model.CartReq
import com.kvr.user.model.Carts
import com.kvr.user.model.CommonResponse
import com.kvr.user.network.ApiServices
import com.kvr.user.network.Response
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartVM : ViewModel() {
    private var _state = MutableStateFlow<Response<Carts>>(Response.Empty())
    val state = _state.asStateFlow()

    fun fetchCart() = viewModelScope.launch {
        _state.value = Response.Loading(true)
        try{
            val result  = ApiServices.getCartsListApi()
            if(result.data?.success == true || result.data?.status == true){
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
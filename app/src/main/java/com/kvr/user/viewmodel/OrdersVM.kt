package com.kvr.user.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.fuel.core.FuelError
import com.kvr.user.model.OrderList
import com.kvr.user.model.Orders
import com.kvr.user.model.ProfileData
import com.kvr.user.network.ApiServices
import com.kvr.user.network.Response
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrdersVM : ViewModel() {
    private var _state = MutableStateFlow<Response<OrderList>>(Response.Empty())
    val state = _state.asStateFlow()

    private var ostate = MutableStateFlow<Response<Orders>>(Response.Empty())
    val orderstate = ostate.asStateFlow()


    fun fetchOrdersList() = viewModelScope.launch {
        _state.value = Response.Loading(true)
        try {
            val result  = ApiServices.getOrdersListApi()
            if(result.data?.status == true){
                _state.value = result
            }
        }catch (e: FuelError){
            e.message?.let { _state.value = Response.Error(it) }
            delay(16)
            _state.value = Response.Loading(false)
        }finally {
            delay(16)
            _state.value = Response.Loading(false)
        }
    }

    fun fetchOrderDetails(orderId:Int = -1) = viewModelScope.launch {
        ostate.value = Response.Loading(true)
        try {
            val result  = ApiServices.getOrdersDetailsApi(orderId)
            if(result.data?.success == true || result.data?.status == true){
                ostate.value = result
            }
        }catch (e: FuelError){
            e.message?.let { ostate.value = Response.Error(it) }
            delay(16)
            ostate.value = Response.Loading(false)
        }finally {
            delay(16)
            ostate.value = Response.Loading(false)
        }
    }

}
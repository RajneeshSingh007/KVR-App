package com.kvr.user.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.fuel.core.FuelError
import com.kvr.user.model.AddressList
import com.kvr.user.model.Brands
import com.kvr.user.network.ApiServices
import com.kvr.user.network.Response
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddressVM: ViewModel() {
    private var _state = MutableStateFlow<Response<AddressList>>(Response.Empty())
    val state = _state.asStateFlow()

    fun fetchAddress() = viewModelScope.launch {
        _state.value = Response.Loading(true)
        try{
            val result  = ApiServices.getAddressListApi()
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
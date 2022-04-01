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
import java.io.File

class CommonVM : ViewModel() {
    private var _state = MutableStateFlow<Response<CommonResponse>>(Response.Empty())
    val state = _state.asStateFlow()

    //update cart api
    fun addUpdateCartApi(cartReq: CartReq, isUpdate: Boolean = false) = viewModelScope.launch {
        _state.value = Response.Loading(true)
        try{
            val result  = ApiServices.addToCartApi(cartReq, isUpdate)
            if(result.data?.status == true){
                _state.value = result
            }else{
                result.data?.let { _state.value =  Response.Error(it.message) }
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

    //remove cart api
    fun removeCartApi(cartId:Int) = viewModelScope.launch {
        _state.value = Response.Loading(true)
        try{
            val result  = ApiServices.removeFromCartApi(cartId = cartId)
            if(result.data?.status == true){
                _state.value = result
            }else{
                result.data?.let { _state.value =  Response.Error(it.message) }
            }
        }catch (e:FuelError){
            e.message?.let { _state.value =  Response.Error(it) }
            delay(16)
            _state.value = Response.Loading(false)
        }finally {
            delay(16)
            _state.value = Response.Loading(false)
        }
    }


    //change password
    fun changePasswordApi(pass: ChangePass) = viewModelScope.launch {
        _state.value = Response.Loading(true)
        try{
            val result  = ApiServices.changePassApi(pass)
            if(result.data?.status == true){
                _state.value = result
            }else{
                result.data?.let { _state.value =  Response.Error(it.message) }
            }
        }catch (e:FuelError){
            e.message?.let { _state.value =  Response.Error(it) }
            delay(16)
            _state.value = Response.Loading(false)
        }finally {
            delay(16)
            _state.value = Response.Loading(false)
        }
    }

    //delete Address
    fun deleteAddressApi(addressId:Int) = viewModelScope.launch {
        _state.value = Response.Loading(true)
        try{
            val result  = ApiServices.deleteAddApi(addressId)
            if(result.data?.status == true){
                _state.value = result
            }else{
                result.data?.let { _state.value =  Response.Error(it.message) }
            }
        }catch (e:FuelError){
            e.message?.let { _state.value =  Response.Error(it) }
            delay(16)
            _state.value = Response.Loading(false)
        }finally {
            delay(16)
            _state.value = Response.Loading(false)
        }
    }

    //add/update Address
    fun addUpdateAddressApi(addressesReq: AddressesReq, isUpdate: Boolean) = viewModelScope.launch {
        _state.value = Response.Loading(true)
        try{
            val result  = ApiServices.addAddApi(addressesReq,isUpdate)
            if(result.data?.status == true){
                _state.value = result
            }else{
                result.data?.let { _state.value =  Response.Error(it.message) }
            }
        }catch (e:FuelError){
            e.message?.let { _state.value =  Response.Error(it) }
            delay(16)
            _state.value = Response.Loading(false)
        }finally {
            delay(16)
            _state.value = Response.Loading(false)
        }
    }

    //place Order
    fun placeOrderApi(placeOrderReq: PlaceOrderReq) = viewModelScope.launch {
        _state.value = Response.Loading(true)
        try{
            val result  = ApiServices.placeOrderApi(placeOrderReq)
            if(result.data?.status == true){
                _state.value = result
            }else{
                result.data?.let { _state.value =  Response.Error(it.message) }
            }
        }catch (e:FuelError){
            e.message?.let { _state.value =  Response.Error(it) }
            delay(16)
            _state.value = Response.Loading(false)
        }finally {
            delay(16)
            _state.value = Response.Loading(false)
        }
    }

    //update Profile
    fun updateProfile(profileReq: ProfileReq, file: File? = null) = viewModelScope.launch {
        _state.value = Response.Loading(true)
        try {
            val result  = ApiServices.updateProfileApi(profileReq, file)
            if(result.data?.status == true){
                _state.value = result
            }else{
                result.data?.let { _state.value =  Response.Error(it.message) }
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
}
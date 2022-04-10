package com.kvr.user.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.fuel.core.FuelError
import com.kvr.user.model.*
import com.kvr.user.network.ApiServices
import com.kvr.user.network.Response
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeVM : ViewModel() {
    private var bannerstate = MutableStateFlow<Response<Banner>>(Response.Empty())
    val bstate = bannerstate.asStateFlow()

    private var _state = MutableStateFlow<Response<Brands>>(Response.Empty())
    val state = _state.asStateFlow()

    private var _pstate = MutableStateFlow<Response<ProductList>>(Response.Empty())
    val pstate = _pstate.asStateFlow()

    //fetch brands
    fun fetchBrands() = viewModelScope.launch {
        _state.value = Response.Loading(true)
       try {
           delay(20)
           val result  = ApiServices.getBrandsApi()
           if(result.data?.status == true){
               _state.value = result
           }
       }catch (e:FuelError){
           e.message?.let { _state.value = Response.Error(it) }
           delay(16)
           _state.value = Response.Loading(false)
       }finally {
           delay(16)
           _state.value = Response.Loading(false)
       }
    }

    //banners
    fun fetchBanners() = viewModelScope.launch {
        bannerstate.value = Response.Loading(true)
        try {
            delay(20)
            val result  = ApiServices.getBannersApi()
            if(result.data?.status == true){
                bannerstate.value = result
            }
        }catch (e:FuelError){
            e.message?.let { bannerstate.value = Response.Error(it) }
            delay(16)
            bannerstate.value = Response.Loading(false)
        }finally {
            delay(16)
            bannerstate.value = Response.Loading(false)
        }
    }

    //products list
    fun fetchProductsType(types:Int = 1,type:String) = viewModelScope.launch {
        _pstate.value = Response.Loading(true)
        try{
            delay(20)
            val result = ApiServices.getHomeProductApi(type)
            if(result.data?.status == true){
                result.data.type = types
                _pstate.value = result
            }
        }catch (e:FuelError){
            e.message?.let { _pstate.value = Response.Error(it) }
            delay(16)
            _pstate.value = Response.Loading(false)
        }finally {
            delay(16)
            _pstate.value = Response.Loading(false)
        }
    }

    //search products
    fun searchProducts(query:String ="") = viewModelScope.launch {
        _pstate.value = Response.Loading(true)
        Log.e("query", query)
        try{
            val result = ApiServices.getSearchProductsApi(query)
            result.data?.type = 3
            _pstate.value = result
        }catch (e:FuelError){
            e.message?.let { _pstate.value = Response.Error(it) }
        }finally {
            delay(16)
            _pstate.value = Response.Loading(false)
        }
    }
}
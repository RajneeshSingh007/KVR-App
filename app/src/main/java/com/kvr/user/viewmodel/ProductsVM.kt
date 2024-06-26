package com.kvr.user.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.fuel.core.FuelError
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

class ProductsVM : ViewModel() {
    //single product
    private var _state = MutableStateFlow<Response<Products>>(Response.Empty())
    val state = _state.asStateFlow()

    //products list
    private var _pstate = MutableStateFlow<Response<ProductDetails>>(Response.Empty())
    val pstate = _pstate.asStateFlow()

    //category
    private var _cstate = MutableStateFlow<Response<CategoryList>>(Response.Empty())
    val cstate = _cstate.asStateFlow()

    //productsList
    fun fetchProductsApiCall(brandsId:Int= 0) = viewModelScope.launch {
        _state.value = Response.Loading(true)
        try{
            val result = ApiServices.getBrandsProductApi(brandsId)
            if(result.data?.status == true){
                _state.value = result
            }
        }catch (e: FuelError){
            e.message?.let { _state.value =  Response.Error(it) }
            delay(16)
            _state.value = Response.Loading(false)
            e.printStackTrace()
        }finally {
            delay(16)
            _state.value = Response.Loading(false)
        }
    }

    //search products
    fun fetchSearchProductsApiCall(searchProducts: SearchProducts) = viewModelScope.launch {
        _state.value = Response.Loading(true)
        try{
            val result = ApiServices.searchProductsApi(searchProducts)
            if(result.data?.status == true){
                _state.value = Response.Success(Products(data = PData(products = result.data.data),"success", true))
            }
        }catch (e: FuelError){
            e.printStackTrace()
            e.message?.let {
                Log.e("Error", it.toString())
                _state.value =  Response.Error(it)
            }
            delay(16)
            _state.value = Response.Loading(false)
        }finally {
            delay(16)
            _state.value = Response.Loading(false)
        }
    }

    //product details
    fun fetchProductsDetailsApiCall(productId:Int= 0) = viewModelScope.launch {
        _pstate.value = Response.Loading(true)
        try {
            val result = ApiServices.getProductDetailsApi(productId)
            if(result.data?.status == true){
                _pstate.value = result
            }
        }catch (e:FuelError){
            e.message?.let { _state.value =  Response.Error(it) }
            delay(16)
            _state.value = Response.Loading(false)
        }finally {
            delay(16)
            _pstate.value = Response.Loading(false)
        }
    }

    fun fetchCategoryListApiCall() = viewModelScope.launch {
        _cstate.value = Response.Loading(true)
        try {
            val result = ApiServices.getCategoryListApi()
            if(result.data?.status == true){
                _cstate.value = result
            }
        }catch (e:FuelError){
            e.message?.let { _state.value =  Response.Error(it) }
            delay(16)
            _state.value = Response.Loading(false)
        }finally {
            delay(16)
            _cstate.value = Response.Loading(false)
        }
    }
}
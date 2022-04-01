package com.kvr.user.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.fuel.core.FuelError
import com.kvr.user.model.Brands
import com.kvr.user.model.Profile
import com.kvr.user.model.ProfileData
import com.kvr.user.network.ApiServices
import com.kvr.user.network.Response
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileVM : ViewModel() {
    private var _state = MutableStateFlow<Response<ProfileData>>(Response.Empty())
    val state = _state.asStateFlow()

    fun fetchProfile() = viewModelScope.launch {
        _state.value = Response.Loading(true)
        try {
            val result  = ApiServices.getProfileApi()
            if(result.data?.status == true){
                result.data.data.let { _state.value = Response.Success(it) }
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
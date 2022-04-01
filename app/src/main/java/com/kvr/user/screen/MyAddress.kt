package com.kvr.user.screen

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kvr.user.BaseApplication
import com.kvr.user.Screen
import com.kvr.user.model.AddressData
import com.kvr.user.model.AddressList
import com.kvr.user.model.AddressesReq
import com.kvr.user.model.CartData
import com.kvr.user.network.Response
import com.kvr.user.screen.common.AddressInfo
import com.kvr.user.screen.common.Header
import com.kvr.user.ui.theme.MontserratRegular
import com.kvr.user.ui.theme.PrimaryColor
import com.kvr.user.ui.theme.RobotoRegular
import com.kvr.user.ui.theme.WhiteColor
import com.kvr.user.utils.Constants
import com.kvr.user.utils.Helpers
import com.kvr.user.viewmodel.AddressVM
import com.kvr.user.viewmodel.CommonVM
import kotlinx.coroutines.launch

@Composable
fun MyAddress(navController: NavHostController,loader: (show:Boolean)-> Unit = {}) {
    val context = LocalContext.current as Activity
    val addressVM = viewModel<AddressVM>()
    val state = addressVM.state.collectAsState().value
    val addressList = remember { mutableStateListOf<AddressData>() }
    val commonVM = viewModel<CommonVM>()
    val commonState = commonVM.state.collectAsState().value
    val removeIndex = remember { mutableStateOf(-1) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = "MyAddress"){
        addressVM.fetchAddress()
    }

    LaunchedEffect(key1 = state){
        when(state){
            is Response.Loading ->{
            }
            is Response.Error ->{
                Helpers.showToast(context, 1, state.error)
            }
            is Response.Success ->{
                state.data?.let {
                    addressList.addAll(it.data)
                }
            }
            else -> {
            }
        }
    }

    LaunchedEffect(key1 = commonState){
        when(commonState){
            is Response.Loading ->{
                loader(commonState.isLoading)
            }
            is Response.Error ->{
                Helpers.showToast(context, 1, commonState.error)
            }
            is Response.Success ->{
                commonState.data?.message.let {
                    if (it != null) {
                        Helpers.showToast(context, 0, it)
                    }
                }
                if(addressList.isNotEmpty()){
                    addressList.remove(addressList[removeIndex.value])
                }
            }
            else -> {
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(color = WhiteColor)) {
        Header(
            navController = navController, drawerClick = {
                //drawerClick()
                navController.popBackStack()
            }, showIcon = false, title = "My Addresses"
        )
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        if(state.isLoading){
            Box(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 56.dp)){
                CircularProgressIndicator()
            }
        }else if(addressList.isNotEmpty()){
            addressList.forEachIndexed{ i, it ->
                val nameMobile = "${Helpers.removeNull(it.first_name)}#${Helpers.removeNull(it.first_name)}#${Helpers.removeNull(it.mobile_no)}"
                val address =  "${Helpers.removeNull(it.address1)}#${Helpers.removeNull(it.address2)}#${Helpers.removeNull(it.city)}#${Helpers.removeNull(it.zipcode)}"
                AddressInfo(showEditBtn =false, name = "${Helpers.removeNull(it.first_name)} ${Helpers.removeNull(it.first_name)} - ${Helpers.removeNull(it.mobile_no)}", address ="${Helpers.removeNull(it.address1)}, ${Helpers.removeNull(it.address2)}, ${Helpers.removeNull(it.city)} - ${Helpers.removeNull(it.zipcode)}", showBtn=true, deleteBtn = {
                    removeIndex.value = i
                    commonVM.deleteAddressApi(it.id)
                }, editBtn = {
                    //Log.e("address", "${address}")
                    navController.navigate("${Screen.AddressScreen.route}/${nameMobile}/${address}/${it.id}")
                }, selected = {
                    scope.launch {
                        BaseApplication.appContext.appPref.putString(Constants.PREF_ADD, "${nameMobile}?${address}")
                        BaseApplication.appContext.appPref.putString(Constants.PREF_ADD_ID, it.id.toString())
                        navController.popBackStack()
                    }
                })
            }
        }

        Spacer(modifier = Modifier.padding(vertical = 10.dp))

        Button(
            onClick = {
                navController.navigate(Screen.AddressScreen.route)
            },  modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(0.dp)
        ) {
            Text(
                text = "Add New address",
                style = TextStyle(
                    color = WhiteColor,
                    fontSize = 15.sp
                ),
                fontFamily = FontFamily(fonts = MontserratRegular),
            )
        }
    }
}
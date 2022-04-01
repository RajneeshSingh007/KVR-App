package com.kvr.user.screen

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.kvr.user.BaseApplication
import com.kvr.user.Screen
import com.kvr.user.model.*
import com.kvr.user.network.Response
import com.kvr.user.screen.common.AddressInfo
import com.kvr.user.screen.common.Header
import com.kvr.user.ui.theme.*
import com.kvr.user.utils.Constants
import com.kvr.user.utils.Helpers
import com.kvr.user.utils.Utils
import com.kvr.user.viewmodel.AddressVM
import com.kvr.user.viewmodel.CommonVM
import com.kvr.user.viewmodel.CouponVM
import kotlinx.coroutines.launch

@Composable
fun CouponScreen(navController: NavHostController) {
    val context = LocalContext.current as Activity
    val couponVM = viewModel<CouponVM>()
    val state = couponVM.state.collectAsState().value
    val couponsList = remember { mutableStateListOf<CouponsData>() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = "CouponScreen"){
        couponVM.fetchCoupons()
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
                    couponsList.addAll(it.data)
                }
            }
            else -> {
            }
        }
    }

    //apply coupons
    fun applyCoupons(couponsData: CouponsData){
        if(couponsData.no_of_times > 0){
            scope.launch {
                val value = "${couponsData.id}#${couponsData.title}#${couponsData.code_name}#${couponsData.discount}#${couponsData.type}"
                BaseApplication.appContext.appPref.putString(Constants.PREF_SELECTED_COUPONS, value)
            }
            navController.popBackStack()
        }else{
            Helpers.showToast(context, 1, "Coupons max limit exceeded")
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
            }, showIcon = false, title = "Coupons"
        )
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        if(state.isLoading){
            Box(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 56.dp)){
                CircularProgressIndicator()
            }
        }else if(couponsList.isNotEmpty()){
            couponsList.forEachIndexed{ i, it ->
                Surface( elevation = 6.dp,
                    color = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            applyCoupons(it)
                        }, shape = RoundedCornerShape(8.dp)) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                                .fillMaxHeight()) {
                            Text(
                                text = it.title,
                                style = TextStyle(
                                    color = PrimaryColor,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Start
                                ),
                                fontFamily = FontFamily(fonts = MontserratMedium),
                            )
                            Spacer(modifier = Modifier.padding(vertical = 2.dp))
                            Text(
                                text = it.code_name,
                                style = TextStyle(
                                    color = GREYCOLOR,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Start
                                ),
                                fontFamily = FontFamily(fonts = MontserratRegular),
                            )
                            Spacer(modifier = Modifier.padding(vertical = 2.dp))
                            Row(horizontalArrangement = Arrangement.SpaceBetween){
                                Box{

                                }
                                Text(
                                    text = it.expiry_date,
                                    style = TextStyle(
                                        color = TextColor,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.End
                                    ),
                                    fontFamily = FontFamily(fonts = MontserratRegular),
                                    modifier = Modifier.padding(end=4.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(vertical = 2.dp))
            }
        }

        Spacer(modifier = Modifier.padding(vertical = 10.dp))
    }
}
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
import androidx.compose.ui.res.stringResource
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
import com.kvr.user.R
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
import com.kvr.user.viewmodel.NotificationVM
import kotlinx.coroutines.launch

@Composable
fun NotificationScreen(navController: NavHostController) {
    val context = LocalContext.current as Activity
    val notificationVM = viewModel<NotificationVM>()
    val state = notificationVM.state.collectAsState().value
    val notifyList = remember { mutableStateListOf<NotifyData>() }

    LaunchedEffect(key1 = "NotificationScreen"){
        notificationVM.fetchNotify()
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
                    notifyList.addAll(it.data)
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
            }, showIcon = false, title = stringResource(R.string.notify)
        )
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        if(state.isLoading){
            Box(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 56.dp)){
                CircularProgressIndicator()
            }
        }else if(notifyList.isNotEmpty()){
            notifyList.forEachIndexed{ i, it ->
                Surface( elevation = 6.dp,
                    color = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
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
                                text = it.message,
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
                                    text = Utils.formatDate(it.updated_at),
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
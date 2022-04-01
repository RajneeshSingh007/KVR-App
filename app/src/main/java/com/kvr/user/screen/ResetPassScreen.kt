package com.kvr.user.screen

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.kvr.user.R
import com.kvr.user.Screen
import com.kvr.user.model.ChangePass
import com.kvr.user.network.Response
import com.kvr.user.screen.common.Header
import com.kvr.user.ui.theme.*
import com.kvr.user.utils.Helpers
import com.kvr.user.viewmodel.CommonVM

@Composable
fun ResetPassScreen(navController: NavHostController,loader: (show:Boolean)-> Unit = {},logout: ()-> Unit = {}) {
    val context = navController.context as Activity
    val newPassword = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val showVisualNewPass = remember { mutableStateOf<VisualTransformation>(
        PasswordVisualTransformation()
    ) }
    val showVisualPass = remember { mutableStateOf<VisualTransformation>(
        PasswordVisualTransformation()
    ) }
    val commonVM = viewModel<CommonVM>()
    val commonState = commonVM.state.collectAsState().value

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
                logout()
            }
            else -> {
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
            .background(color = WhiteColor),
    ) {
        Header(navController =navController,drawerClick = {
            //drawerClick()
            navController.popBackStack()
        }, title = "Change Password", showIcon = false)
       Column(Modifier.padding(horizontal = 16.dp)) {
           Spacer(modifier = Modifier.height(30.dp))
           Text(
               text = "New Password",
               style = TextStyle(
                   color = TextColor,
                   fontSize = 14.sp
               ),
               fontFamily = FontFamily(fonts = MontserratRegular)
           )
           Spacer(modifier = Modifier.height(10.dp))
           TextField(
               modifier = Modifier
                   .clip(shape = RoundedCornerShape(8.dp))
                   .background(color = Color(0xFFf3f3f3))
                   .fillMaxWidth(),
               colors = TextFieldDefaults.textFieldColors(
                   backgroundColor = Color(0xFFf3f3f3),
                   focusedIndicatorColor = Color.Transparent,
                   unfocusedIndicatorColor = Color.Transparent,
                   textColor = Color.Black
               ),
               keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
               value = password.value,
               onValueChange = { password.value = it },
               textStyle = TextStyle(
                   color = BlackColor,
                   fontSize = 16.sp,
                   fontFamily = FontFamily(fonts = MontserratRegular),
                   textAlign = TextAlign.Start
               ),
               visualTransformation = showVisualPass.value,
               trailingIcon={
                   IconButton(onClick = {
                       showVisualPass.value = if (showVisualPass.value == PasswordVisualTransformation()) VisualTransformation.None else PasswordVisualTransformation()
                   }) {
                       FaIcon(faIcon = if(showVisualPass.value == VisualTransformation.None) FaIcons.EyeSlashRegular else FaIcons.EyeRegular, size = 20.dp, tint = TextColor)
                   }
               },
               placeholder={
                   Text(
                       text = "Enter New Password",
                       style = TextStyle(
                           color = Color(0xFF565a5e),
                           fontSize = 14.sp,
                           textAlign = TextAlign.Start
                       ),
                       fontFamily = FontFamily(fonts = MontserratRegular),
                       modifier = Modifier.padding(start = 0.dp)
                   )
               },
           )
           Spacer(modifier = Modifier.height(16.dp))
           Text(
               text = "Confirm Password",
               style = TextStyle(
                   color = TextColor,
                   fontSize = 14.sp
               ),
               fontFamily = FontFamily(fonts = MontserratRegular)
           )
           Spacer(modifier = Modifier.height(10.dp))
           TextField(
               modifier = Modifier
                   .clip(shape = RoundedCornerShape(8.dp))
                   .background(color = Color(0xFFf3f3f3))
                   .fillMaxWidth(),
               colors = TextFieldDefaults.textFieldColors(
                   backgroundColor = Color(0xFFf3f3f3),
                   focusedIndicatorColor = Color.Transparent,
                   unfocusedIndicatorColor = Color.Transparent,
                   textColor = Color.Black
               ),
               keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
               value = newPassword.value,
               onValueChange = { newPassword.value = it },
               textStyle = TextStyle(
                   color = BlackColor,
                   fontSize = 16.sp,
                   fontFamily = FontFamily(fonts = MontserratRegular),
                   textAlign = TextAlign.Start
               ),
               visualTransformation = showVisualNewPass.value,
               trailingIcon={
                   IconButton(onClick = {
                       showVisualNewPass.value = if (showVisualNewPass.value == PasswordVisualTransformation()) VisualTransformation.None else PasswordVisualTransformation()
                   }) {
                       FaIcon(faIcon = if(showVisualNewPass.value == VisualTransformation.None) FaIcons.EyeSlashRegular else FaIcons.EyeRegular, size = 20.dp, tint = TextColor)
                   }
               },
               placeholder={
                   Text(
                       text = "Enter Confirm Password",
                       style = TextStyle(
                           color = Color(0xFF565a5e),
                           fontSize = 14.sp,
                           textAlign = TextAlign.Start
                       ),
                       fontFamily = FontFamily(fonts = MontserratRegular),
                       modifier = Modifier.padding(start = 0.dp)
                   )
               },
           )
           Spacer(modifier = Modifier.height(30.dp))
           Button(
               onClick = {
                    if (password.value == "") {
                        Helpers.showToast(context, 1, "Please, Enter Password")
                    } else if (newPassword.value == "") {
                        Helpers.showToast(
                            context,
                            1,
                            "Please, Enter Confirm Password"
                        )
                    }else if (password.value  != newPassword.value) {
                       Helpers.showToast(
                           context,
                           1,
                           "Failed to match password"
                       )
                   } else {
                        commonVM.changePasswordApi(ChangePass(password = password.value, c_password = newPassword.value))
                   }
               }, modifier = Modifier
                   .fillMaxWidth()
                   .height(50.dp)
                   .clip(shape = RoundedCornerShape(8.dp)),
           ) {
               Text(
                   text = "Submit",
                   style = TextStyle(
                       color = WhiteColor,
                       fontSize = 16.sp
                   ),
                   fontFamily = FontFamily(fonts = MontserratRegular),
               )
           }
           Spacer(modifier = Modifier.height(20.dp))
       }
    }
}
package com.kvr.user.screen

import android.app.Activity
import android.util.Patterns
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.github.kittinunf.fuel.core.FuelManager
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.kvr.user.R
import com.kvr.user.Screen
import com.kvr.user.ui.theme.*
import com.kvr.user.utils.Helpers
import com.kvr.user.viewmodel.AuthVM
import com.kvr.user.model.LoginReq
import com.kvr.user.model.Otp
import com.kvr.user.model.OtpReq
import com.kvr.user.network.Response
import com.kvr.user.view.BackHandler

@Composable
fun SignIn(navController: NavHostController, loader: (show:Boolean)-> Unit = {}) {
    val context = LocalContext.current as Activity
    val authVM = viewModel<AuthVM>()
    val state = authVM.state.collectAsState().value
    val username = remember { mutableStateOf("") }
    val whatsappNumber = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val showVisualPass = remember { mutableStateOf<VisualTransformation>(PasswordVisualTransformation()) }
    val otpState = authVM._otpState.collectAsState().value
    val otpModel = remember { mutableStateOf<Otp?>(null) }
    val isOtpView = remember { mutableStateOf(false) }
    val otp = remember { mutableStateOf("") }

    //otp state change
    LaunchedEffect(key1 = otpState){
        when (otpState) {
            is Response.Loading -> {
                loader(otpState.isLoading)
            }
            is Response.Error -> {
                Helpers.showToast(context, 1, otpState.error)
            }
            is Response.Success -> {
                otpState.data?.let {
                    if(it.status){
                        isOtpView.value = true
                        Helpers.showToast(context, 0, it.message.toString())
                        otpModel.value = it
                    }
                }
            }
            else -> {
            }
        }
    }

    fun login(){
        if (username.value.isEmpty() && whatsappNumber.value.isEmpty()) {
            Helpers.showToast(context, 1, context.getString(R.string.sign_username_error))
        }
//        else if(username.value.contains('@') && !Patterns.EMAIL_ADDRESS.matcher(username.value).matches()){
//            Helpers.showToast(context,1 , context.getString(R.string.valid_email))
//        }
        else if (isOtpView.value && password.value.isEmpty()) {
            Helpers.showToast(
                context,
                1,
                context.getString(R.string.valid_password)
            )
        } else if (isOtpView.value && otpModel.value != null && otpModel.value?.data.toString() != password.value) {
            Helpers.showToast(
                context,
                1,
                "Failed to match OTP Number"
            )
        } else {
            val loginReq = LoginReq(mobile_number = username.value, whatsapp_number = whatsappNumber.value, otp = password.value)
            if(isOtpView.value){
                authVM.loginApiCall(loginReq)
            }else{
                authVM.loginOtpCall(loginReq)
            }
        }
    }

   LaunchedEffect(key1 = state){
       when(state){
           is Response.Loading ->{
               loader(state.isLoading)
           }
           is Response.Error ->{
               Helpers.showToast(context, 1, state.error)
           }
           is Response.Success ->{
               state.data?.message.let {
                   if (it != null) {
                       Helpers.showToast(context, 0, it)
                   }
               }
               state.data?.let{
                   FuelManager.instance.baseHeaders = mapOf("Authorization" to "Bearer ${it.data.token}")
               }
               navController.navigate(Screen.HomeScreen.route){
                   popUpTo(Screen.SignIn.route){
                       inclusive = true
                   }
               }
           }
           else -> {
           }
       }
   }

    Column(
        modifier = Modifier
            .fillMaxWidth(1.0f)
            .fillMaxHeight(1.0f)
            .padding(all = 16.dp)
            .background(color = WhiteColor),
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Image(
            painter = painterResource(id = R.drawable.signin_profile),
            contentDescription = "logo",
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(30.dp))

        if(isOtpView.value){
            Text(
                text = stringResource(R.string.otp_heading),
                style = TextStyle(
                    color = TextColor,
                    fontSize = 16.sp
                ),
                fontFamily = FontFamily(fonts = MontserratRegular),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = stringResource(R.string.otp_heading1),
                style = TextStyle(
                    color = TextColor,
                    fontSize = 16.sp
                ),
                fontFamily = FontFamily(fonts = MontserratRegular),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = stringResource(R.string.otp_number),
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                value = password.value,
                onValueChange = {it ->
                    val phoneParse = it.filter { it.isDigit() }
                    if(phoneParse.length < 7){
                        password.value =  phoneParse
                    }
                },
                textStyle = TextStyle(
                    color = BlackColor,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(fonts = MontserratMedium),
                    textAlign = TextAlign.Start
                ),
//                visualTransformation = showVisualPass.value,
//                trailingIcon={
//                    IconButton(onClick = {
//                        showVisualPass.value = if (showVisualPass.value == PasswordVisualTransformation()) VisualTransformation.None else PasswordVisualTransformation()
//                    }) {
//                        FaIcon(faIcon = if(showVisualPass.value == VisualTransformation.None) FaIcons.EyeSlashRegular else FaIcons.EyeRegular, size = 20.dp, tint = TextColor)
//                    }
//                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                Box(modifier = Modifier
                    .weight(0.1f)
                    .align(alignment = Alignment.CenterVertically)){
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.weight(3.0f)){
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = TextColor,
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(fonts = MontserratRegular),
                                )
                            ) {
                                append(stringResource(R.string.failed_otp))
                            }
                        },
                        style = TextStyle(
                            textAlign = TextAlign.Start
                        ),
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = PrimaryColor,
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(fonts = MontserratBold)
                                ),

                                ) {
                                append(" "+stringResource(R.string.resend_otp))
                            }
                        },
                        modifier = Modifier
                            .align(alignment = Alignment.CenterVertically)
                            .clickable {
                                val loginReq = LoginReq(mobile_number = username.value, whatsapp_number = whatsappNumber.value, otp = password.value)
                                authVM.loginOtpCall(loginReq)
                            },
                        style = TextStyle(
                            textAlign = TextAlign.Start
                        )
                    )
                }
                Box(modifier = Modifier
                    .weight(0.1f)
                    .align(alignment = Alignment.CenterVertically)){
                }
            }
        }else{
            Text(
                text = stringResource(R.string.sign_in_heading),
                style = TextStyle(
                    color = TextColor,
                    fontSize = 16.sp
                ),
                fontFamily = FontFamily(fonts = MontserratRegular),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = stringResource(R.string.sign_in_heading1),
                style = TextStyle(
                    color = TextColor,
                    fontSize = 16.sp
                ),
                fontFamily = FontFamily(fonts = MontserratRegular),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = stringResource(R.string.sinin_placholder_email),
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                value = username.value,
                onValueChange = {it ->
                    val phoneParse = it.filter { it.isDigit() }
                    if(phoneParse.length < 11){
                        username.value =  phoneParse
                    }
                },
                textStyle = TextStyle(
                    color = BlackColor,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(fonts = MontserratMedium),
                    textAlign = TextAlign.Start
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.sinin_placholder_password),
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                value = whatsappNumber.value,
                onValueChange = {it ->
                    val phoneParse = it.filter { it.isDigit() }
                    if(phoneParse.length < 11){
                        whatsappNumber.value =  phoneParse
                    }
                },
                textStyle = TextStyle(
                    color = BlackColor,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(fonts = MontserratMedium),
                    textAlign = TextAlign.Start
                )
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = {
                login()
            }, modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(shape = RoundedCornerShape(8.dp)),
        ) {
            Text(
                text = if(isOtpView.value) stringResource(R.string.submit) else  stringResource(R.string.login),
                style = TextStyle(
                    color = WhiteColor,
                    fontSize = 16.sp
                ),
                fontFamily = FontFamily(fonts = MontserratRegular),
            )
        }
//        Spacer(modifier = Modifier.height(16.dp))
//        Text(
//            buildAnnotatedString {
//                withStyle(
//                    style = SpanStyle(
//                        color = PrimaryColor,
//                        fontSize = 14.sp,
//                        fontFamily = FontFamily(fonts = MontserratRegular),
//                    )
//                ) {
//                    append(stringResource(R.string.forgot_pass))
//                }
//            },
//            style = TextStyle(
//                textAlign = TextAlign.Start
//            ),
//            modifier = Modifier
//                .align(alignment = Alignment.End)
//                .padding(end = 4.dp)
//                .clickable {
//                    navController.navigate(Screen.ForgotPass.route)
//                }
//        )
        Spacer(modifier = Modifier.height(16.dp))
//        Row{
//            Box(modifier = Modifier
//                .weight(1.5f)
//                .align(alignment = Alignment.CenterVertically)){
//                Divider(color = Color(0xFFE0DFDF), thickness = 0.8.dp)
//            }
//            Box(modifier = Modifier.weight(0.5f), contentAlignment = Alignment.Center){
//                Text(
//                    text = stringResource(R.string.or),
//                    style = TextStyle(
//                        color = HeadingColor,
//                        fontSize = 15.sp
//                    ),
//                    fontFamily = FontFamily(fonts = MontserratRegular),
//                    textAlign = TextAlign.Start,
//                )
//            }
//            Box(modifier = Modifier
//                .weight(1.5f)
//                .align(alignment = Alignment.CenterVertically)){
//                Divider(color = Color(0xFFE0DFDF), thickness = 0.8.dp)
//            }
//        }
//        Spacer(modifier = Modifier.height(24.dp))
//        OutLinedBtn(modifier = Modifier
//            .fillMaxWidth()
//            .align(alignment = Alignment.CenterHorizontally),icon = R.drawable.profile,btnText = stringResource(
//                        R.string.login_customer), click = {
//            login()
//        })
//        OutLinedBtn(modifier = Modifier
//            .fillMaxWidth()
//            .align(alignment = Alignment.CenterHorizontally),icon = R.drawable.distributor,btnText = stringResource(
//                        R.string.login_distributor), click = {
//            login()
//        })
//        OutLinedBtn(modifier = Modifier
//            .fillMaxWidth()
//            .align(alignment = Alignment.CenterHorizontally),icon = R.drawable.wholesale,btnText = stringResource(
//                        R.string.login_wholesaler), click = {
//            login()
//        })
//        Spacer(modifier = Modifier.height(20.dp))
//        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
//            Box(modifier = Modifier
//                .weight(0.1f)
//                .align(alignment = Alignment.CenterVertically)){
//            }
//            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.weight(3.0f)){
//                Text(
//                    buildAnnotatedString {
//                        withStyle(
//                            style = SpanStyle(
//                                color = TextColor,
//                                fontSize = 14.sp,
//                                fontFamily = FontFamily(fonts = MontserratRegular),
//                            )
//                        ) {
//                            append(stringResource(R.string.no_account))
//                        }
//                    },
//                    style = TextStyle(
//                        textAlign = TextAlign.Start
//                    ),
//                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
//                )
//                Text(
//                    buildAnnotatedString {
//                        withStyle(
//                            style = SpanStyle(
//                                color = PrimaryColor,
//                                fontSize = 14.sp,
//                                fontFamily = FontFamily(fonts = MontserratBold)
//                            ),
//
//                            ) {
//                            append(" "+stringResource(R.string.sign_up))
//                        }
//                    },
//                    modifier = Modifier
//                        .align(alignment = Alignment.CenterVertically)
//                        .clickable {
//                            navController.navigate(Screen.Signup.route)
//                        },
//                    style = TextStyle(
//                        textAlign = TextAlign.Start
//                    )
//                )
//            }
//            Box(modifier = Modifier
//                .weight(0.1f)
//                .align(alignment = Alignment.CenterVertically)){
//            }
//        }
        Spacer(modifier = Modifier.height(40.dp))

        BackHandler(enabled = isOtpView.value, onBack = {
            isOtpView.value = false
        })
    }

}

@Composable
fun OutLinedBtn(modifier:Modifier, click:() -> Unit = {},icon:Int,btnText:String = ""){
    OutlinedButton(
        onClick = {
            click();
        }, modifier = modifier,
        elevation = ButtonDefaults.elevation(
            defaultElevation = 3.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        ),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFd6d6d6)),
        border = BorderStroke(1.dp, color = Color(0xFFd6d6d6)),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start,
            modifier=  Modifier.weight(3.5f, fill = true)) {
            Box(modifier= Modifier
                .weight(1f)
                .align(alignment = Alignment.CenterVertically)){
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "",
                    modifier = Modifier.size(30.dp)
                )
            }
            Box(modifier=  Modifier.weight(2.5f)){
                Text(
                    text = btnText,
                    style = TextStyle(
                        color = HeadingColor,
                        fontSize = 15.sp
                    ),
                    fontFamily = FontFamily(fonts = MontserratRegular),
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

package com.kvr.user.screen

import android.app.Activity
import android.util.Patterns
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
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.kvr.user.R
import com.kvr.user.Screen
import com.kvr.user.model.Otp
import com.kvr.user.model.OtpReq
import com.kvr.user.model.RegisterReq
import com.kvr.user.network.Response
import com.kvr.user.ui.theme.*
import com.kvr.user.utils.Helpers
import com.kvr.user.view.BackHandler
import com.kvr.user.viewmodel.AuthVM

@Composable
fun Signup(navController: NavHostController, loader: (show:Boolean)-> Unit = {}) {
    val userRoleList = listOf<String>("", stringResource(R.string.distributor),stringResource(R.string.wholesaler))
    val context = navController.context as Activity
    val authVM = viewModel<AuthVM>()
    val state = authVM.registeState.collectAsState().value
    val name = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val phone = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val showVisualPass = remember { mutableStateOf<VisualTransformation>(PasswordVisualTransformation()) }
    val showVisualCPass = remember { mutableStateOf<VisualTransformation>(PasswordVisualTransformation()) }
    val role = remember { mutableStateOf(1) }
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

    //register state
    LaunchedEffect(key1 = state){
        when (state) {
            is Response.Loading -> {
                loader(state.isLoading)
            }
            is Response.Error -> {
                Helpers.showToast(context, 1, state.error)
            }
            is Response.Success -> {
                state.data?.message.let {
                    if (it != null) {
                        Helpers.showToast(context, 0, it)
                    }
                }
                loader(false)
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
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.align(alignment = Alignment.CenterHorizontally)){
            Image(
                painter = painterResource(id = R.drawable.signup),
                contentDescription = "logo",
                modifier = Modifier
                    .size(100.dp)
                    //.align(Alignment.CenterHorizontally)
            )
            Box( modifier = Modifier
                .size(100.dp)
                .background(color = Color.Transparent)
                .align(alignment = Alignment.BottomEnd),contentAlignment = Alignment.BottomEnd){
                IconButton(onClick = { /*TODO*/ }) {
                    FaIcon(faIcon = FaIcons.PlusCircle, size = 36.dp, tint = PrimaryColor)
                }
            }
        }
        //signup view
        if(!isOtpView.value){
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = stringResource(R.string.signup_heading),
                style = TextStyle(
                    color = TextColor,
                    fontSize = 16.sp
                ),
                fontFamily = FontFamily(fonts = MontserratRegular),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = stringResource(R.string.signup_heading1),
                style = TextStyle(
                    color = TextColor,
                    fontSize = 16.sp
                ),
                fontFamily = FontFamily(fonts = MontserratRegular),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = stringResource(R.string.name),
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                value = name.value,
                onValueChange = { name.value = it.filter { it.isLetter() || it.isWhitespace() } },
                textStyle = TextStyle(
                    color = BlackColor,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(fonts = MontserratMedium),
                    textAlign = TextAlign.Start
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.last_name),
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                value = lastName.value,
                onValueChange = { lastName.value = it.filter { it.isLetter() || it.isWhitespace()} },
                textStyle = TextStyle(
                    color = BlackColor,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(fonts = MontserratMedium),
                    textAlign = TextAlign.Start
                ),
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.phone_number),
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
                value = phone.value,
                onValueChange = {it ->
                    val phoneParse = it.filter { it.isDigit() }
                    if(phoneParse.length < 11){
                        phone.value =  phoneParse
                    }
                },
                textStyle = TextStyle(
                    color = BlackColor,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(fonts = MontserratMedium),
                    textAlign = TextAlign.Start
                ),
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.email),
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                value = email.value,
                onValueChange = { email.value = it },
                textStyle = TextStyle(
                    color = BlackColor,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(fonts = MontserratMedium),
                    textAlign = TextAlign.Start
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.password),
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
                    fontFamily = FontFamily(fonts = MontserratMedium),
                    textAlign = TextAlign.Start
                ),
                visualTransformation = showVisualPass.value,
                trailingIcon={
                    IconButton(onClick = {
                        showVisualPass.value = if (showVisualPass.value == PasswordVisualTransformation()) VisualTransformation.None else PasswordVisualTransformation()
                    }) {
                        FaIcon(faIcon = if(showVisualPass.value == VisualTransformation.None) FaIcons.EyeSlashRegular else FaIcons.EyeRegular, size = 20.dp, tint = TextColor)
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.confirm_password),
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
                value = confirmPassword.value,
                onValueChange = { confirmPassword.value = it },
                textStyle = TextStyle(
                    color = BlackColor,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(fonts = MontserratMedium),
                    textAlign = TextAlign.Start
                ),
                visualTransformation = showVisualCPass.value,
                trailingIcon={
                    IconButton(onClick = {
                        showVisualCPass.value = if (showVisualCPass.value == PasswordVisualTransformation()) VisualTransformation.None else PasswordVisualTransformation()
                    }) {
                        FaIcon(faIcon = if(showVisualCPass.value == VisualTransformation.None) FaIcons.EyeSlashRegular else FaIcons.EyeRegular, size = 20.dp, tint = TextColor)
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.select_role),
                style = TextStyle(
                    color = TextColor,
                    fontSize = 14.sp
                ),
                fontFamily = FontFamily(fonts = MontserratRegular)
            )
            Spacer(modifier = Modifier.height(10.dp))
            com.kvr.user.widget.DropdownMenu(items = userRoleList, placeholder = ""){ i, it -> role.value = i+1}
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = {
                    if (name.value.isEmpty()) {
                        Helpers.showToast(context, 1, "Please, Enter First Name")
                    }else if (lastName.value.isEmpty()) {
                        Helpers.showToast(context, 1, "Please, Enter Last Name")
                    }else if (phone.value.isEmpty()) {
                        Helpers.showToast(context, 1, "Please, Enter Phone Number")
                    }else if (phone.value.length < 10) {
                        Helpers.showToast(context, 1, "Please, Enter Valid Phone Number")
                    }else if(email.value.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email.value).matches()){
                        Helpers.showToast(context,1 , "Please, Enter Valid Email")
                    }else if (password.value == "") {
                        Helpers.showToast(
                            context,
                            1,
                            "Please, Enter Password"
                        )
                    }else if (confirmPassword.value == "") {
                        Helpers.showToast(
                            context,
                            1,
                            "Please, Enter Confirm Password"
                        )
                    } else if (password.value != confirmPassword.value) {
                        Helpers.showToast(
                            context,
                            1,
                            "Failed to match password"
                        )
                    }  else {
                        authVM.registerApiCall(RegisterReq(role_id = role.value, phone=phone.value, first_name = name.value, last_name = lastName.value, password = password.value, c_password = confirmPassword.value, email = email.value))
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(shape = RoundedCornerShape(8.dp)),
            ) {
                Text(
                    text = stringResource(R.string.signup_btn),
                    style = TextStyle(
                        color = WhiteColor,
                        fontSize = 16.sp
                    ),
                    fontFamily = FontFamily(fonts = MontserratRegular),
                )
            }
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
                                append(stringResource(R.string.already_account))
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
                                append(stringResource(R.string.login_space))
                            }
                        },
                        modifier = Modifier
                            .align(alignment = Alignment.CenterVertically)
                            .clickable {
                                navController.navigate(Screen.SignIn.route)
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
            //otp view
            Spacer(modifier = Modifier.height(30.dp))
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
                value = otp.value,
                onValueChange = {
                    val filterVal = it.filter { it.isDigit() }
                    if(filterVal.length < 7) {
                        otp.value = filterVal
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
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = {
                    if (otp.value.isEmpty()) {
                        Helpers.showToast(context, 1, "Please, Enter OTP Number")
                    }else if (otpModel.value != null && otpModel.value?.data.toString() != otp.value) {
                        Helpers.showToast(
                            context,
                            1,
                            "Failed to match OTP Number"
                        )
                    }  else {
                        authVM.verifyOtpApiCall(OtpReq(phone=phone.value, otp = otp.value))
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(shape = RoundedCornerShape(8.dp)),
            ) {
                Text(
                    text = stringResource(R.string.submit),
                    style = TextStyle(
                        color = WhiteColor,
                        fontSize = 16.sp
                    ),
                    fontFamily = FontFamily(fonts = MontserratRegular),
                )
            }
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
                                append(stringResource(R.string.resend_otp))
                            }
                        },
                        modifier = Modifier
                            .align(alignment = Alignment.CenterVertically)
                            .clickable {
                                authVM.reSendOtpCall(OtpReq(phone=phone.value, otp = otp.value))
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
        }
        Spacer(modifier = Modifier.height(40.dp))
    }

    BackHandler(enabled = isOtpView.value, onBack = {
        isOtpView.value = true
    })
}

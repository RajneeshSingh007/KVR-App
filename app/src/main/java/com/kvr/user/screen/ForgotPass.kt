package com.kvr.user.screen


import android.app.Activity
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.kvr.user.R
import com.kvr.user.Screen
import com.kvr.user.network.Response
import com.kvr.user.screen.common.Header
import com.kvr.user.screen.common.SupportButton
import com.kvr.user.ui.theme.*
import com.kvr.user.utils.Helpers
import com.kvr.user.viewmodel.CommonVM
import com.kvr.user.widget.Border
import com.kvr.user.widget.Borders
import com.kvr.user.widget.border

@Composable
fun ForgotPass(navController: NavHostController,loader: (show:Boolean)-> Unit = {}) {
    val context = navController.context as Activity
    val email = remember { mutableStateOf("") }
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
                navController.navigate(Screen.ForgotSuccess.route)
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
        Spacer(modifier = Modifier.height(56.dp))
        Image(
            painter = painterResource(id = R.drawable.forgot),
            contentDescription = "logo",
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "Please enter your registered email",
            style = TextStyle(
                color = TextColor,
                fontSize = 16.sp
            ),
            fontFamily = FontFamily(fonts = MontserratRegular),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = "below to reset new password",
            style = TextStyle(
                color = TextColor,
                fontSize = 16.sp
            ),
            fontFamily = FontFamily(fonts = MontserratRegular),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "Email",
            style = TextStyle(
                color = TextColor,
                fontSize = 14.sp
            ),
            fontFamily = FontFamily(fonts = MontserratRegular)
        )
        Spacer(modifier = Modifier.height(16.dp))
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
        Button(
            onClick = {
                if (email.value == "") {
                    Helpers.showToast(context, 1, "Please, Enter Email")
                }else if(email.value.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email.value).matches()){
                    Helpers.showToast(context,1 , "Please, Enter Valid Email")
                }else {
                    commonVM.forgotPass(com.kvr.user.model.ForgotPass(email = email.value))
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(shape = RoundedCornerShape(8.dp)),
        ) {
            Text(
                text = "Send",
                style = TextStyle(
                    color = WhiteColor,
                    fontSize = 16.sp
                ),
                fontFamily = FontFamily(fonts = MontserratRegular),
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
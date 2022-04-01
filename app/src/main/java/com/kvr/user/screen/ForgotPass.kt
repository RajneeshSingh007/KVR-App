package com.kvr.user.screen


import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.navigation.NavHostController
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.kvr.user.R
import com.kvr.user.Screen
import com.kvr.user.screen.common.Header
import com.kvr.user.screen.common.SupportButton
import com.kvr.user.ui.theme.*
import com.kvr.user.widget.Border
import com.kvr.user.widget.Borders
import com.kvr.user.widget.border

@Composable
fun ForgotPass(navController: NavHostController) {
    val context = navController.context as Activity
    val email = remember { mutableStateOf("") }

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
            text = "below to receive new password",
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
//                if (email.value == "") {
//                    Helpers.showToast(context, "error", "Please, Enter Email")
//                }else {
//                }
                navController.navigate(Screen.ForgotSuccess.route)
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
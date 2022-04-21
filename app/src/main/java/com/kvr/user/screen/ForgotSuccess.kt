package com.kvr.user.screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.kvr.user.R
import com.kvr.user.Screen
import com.kvr.user.ui.theme.*

@Composable
fun ForgotSuccess(navController: NavHostController) {
    Column( modifier = Modifier
        .fillMaxWidth(1.0f)
        .fillMaxHeight(1.0f)
        .padding(all = 16.dp)
        .background(color = WhiteColor),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.padding(vertical = 36.dp))
        Image(painter = painterResource(R.drawable.email_sent), contentDescription = "success", modifier = Modifier.size(100.dp))
        Spacer(modifier = Modifier.padding(vertical = 16.dp))
        Text(
            text = "Email has been sent",
            style = TextStyle(
                color = HeadingColor,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            ),
            fontFamily = FontFamily(fonts = MontserratBold),
        )
        Spacer(modifier = Modifier.padding(vertical = 16.dp))
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color(0xFF565a5e),
                        fontSize = 13.sp,
                        fontFamily = FontFamily(fonts = MontserratRegular),
                    )
                ) {
                    append("Your new password has been sent")
                }
                withStyle(
                    style = SpanStyle(
                        color = Color(0xFF565a5e),
                        fontSize = 13.sp,
                        fontFamily = FontFamily(fonts = MontserratRegular)
                    )
                ) {
                    append(" please check your email")
                }
            },
            style = TextStyle(
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.padding(vertical = 24.dp))
        Button(
            onClick = {
                navController.navigate(Screen.SignIn.route)
            }, modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(shape = RoundedCornerShape(4.dp)),
        ) {
            Text(
                text = stringResource(id = R.string.login),
                style = TextStyle(
                    color = WhiteColor,
                    fontSize = 16.sp
                ),
                fontFamily = FontFamily(fonts = MontserratRegular),
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 56.dp))
    }
}
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
fun PaymentSuccess(navController: NavHostController, amount: String?="0.00") {
    Column( modifier = Modifier
        .fillMaxWidth(1.0f)
        .fillMaxHeight(1.0f)
        .padding(all = 16.dp)
        .background(color = WhiteColor),
    verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.padding(vertical = 56.dp))
        Image(painter = painterResource(R.drawable.success), contentDescription = "success", modifier = Modifier.size(200.dp))
        Text(
            text = stringResource(R.string.payment_success),
            style = TextStyle(
                color = HeadingColor,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            ),
            fontFamily = FontFamily(fonts = MontserratBold),
        )
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color(0xFF565a5e),
                        fontSize = 13.sp,
                        fontFamily = FontFamily(fonts = MontserratRegular),
                    )
                ) {
                    append(stringResource(R.string.payment_success_heading1))
                }
                withStyle(
                    style = SpanStyle(
                        color = BlackColor,
                        fontSize = 13.sp,
                        fontFamily = FontFamily(fonts = MontserratBold)
                    )
                ) {
                    append(" ₹${amount} ")
                }
                withStyle(
                    style = SpanStyle(
                        color = Color(0xFF565a5e),
                        fontSize = 13.sp,
                        fontFamily = FontFamily(fonts = MontserratRegular)
                    )
                ) {
                    append(stringResource(R.string.payment_success_heading2))
                }
            },
            style = TextStyle(
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.padding(vertical = 6.dp))
//        Text(
//            text = "Order ID: 17647647",
//            style = TextStyle(
//                color = Color(0xFF565a5e),
//                fontSize = 14.sp,
//                textAlign = TextAlign.Center
//            ),
//            fontFamily = FontFamily(fonts = MontserratRegular),
//        )
//        Spacer(modifier = Modifier.padding(vertical = 24.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
            Button(
                onClick = {
                    navController.navigate("${Screen.OrderScreen.route}/1"){
                        popUpTo(Screen.PaymentSuccess.route){
                            inclusive = true
                        }
                    }
                }, modifier = Modifier
                    .weight(0.45f)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFe1e1e1),
                    contentColor = Color(0xFFe1e1e1)
                ),
                shape = RoundedCornerShape(2.dp)

            ) {
                Text(
                    text = stringResource(R.string.track_order_status),
                    style = TextStyle(
                        color = PrimaryColor,
                        fontSize = 14.sp
                    ),
                    fontFamily = FontFamily(fonts = MontserratRegular),
                )
            }
            Spacer(modifier = Modifier.weight(0.1f))
            Button(
                onClick = {
                          navController.navigate(Screen.HomeScreen.route){
                              popUpTo(Screen.PaymentSuccess.route){
                                  inclusive = true
                              }
                          }
                }, modifier = Modifier
                    .weight(0.45f)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFe1e1e1),
                    contentColor = Color(0xFFe1e1e1)
                ),
                shape = RoundedCornerShape(2.dp)

            ) {
                Text(
                    text = stringResource(R.string.b2home),
                    style = TextStyle(
                        color = PrimaryColor,
                        fontSize = 14.sp
                    ),
                    fontFamily = FontFamily(fonts = MontserratRegular),
                )
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 56.dp))
    }
}
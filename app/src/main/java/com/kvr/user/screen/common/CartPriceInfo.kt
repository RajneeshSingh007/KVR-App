package com.kvr.user.screen.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kvr.user.model.CouponsData
import com.kvr.user.ui.theme.*

@Composable
fun CartPriceInfo(itemPrice:String= "00",shippingPrice:String="00", totalPrice:String="00", couponsData: CouponsData? = null){
    Surface( elevation = 6.dp,
        color = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .fillMaxHeight()) {
                Text(
                    text = "Pricing:",
                    style = TextStyle(
                        color = BlackColor,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    ),
                    fontFamily = FontFamily(fonts = MontserratMedium),
                )
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = "Price (2 items)",
                        style = TextStyle(
                            color = PrimaryColor,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        ),
                        fontFamily = FontFamily(fonts = RobotoBold),
                    )
                    Text(
                        text = "₹${itemPrice}",
                        style = TextStyle(
                            color = BlackColor,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        ),
                        fontFamily = FontFamily(fonts = RobotoRegular),
                    )
                }
                Spacer(modifier = Modifier.padding(vertical = 2.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = "Shipping Charges",
                        style = TextStyle(
                            color = PrimaryColor,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        ),
                        fontFamily = FontFamily(fonts = RobotoBold),
                    )
                    Text(
                        text = "₹${shippingPrice}",
                        style = TextStyle(
                            color = BlackColor,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        ),
                        fontFamily = FontFamily(fonts = RobotoRegular),
                    )
                }
                couponsData?.let {
                    Spacer(modifier = Modifier.padding(vertical = 2.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            text = "Coupon Applied",
                            style = TextStyle(
                                color = PrimaryColor,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            ),
                            fontFamily = FontFamily(fonts = RobotoBold),
                        )
                        Text(
                            text = "${it.code_name} - ${if(it.type == "percentage") "" else "₹"}${it.discount}${if(it.type == "percentage") "%" else ""}",
                            style = TextStyle(
                                color = BlackColor,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            ),
                            fontFamily = FontFamily(fonts = RobotoRegular),
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(vertical = 2.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = "Total",
                        style = TextStyle(
                            color = PrimaryColor,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center
                        ),
                        fontFamily = FontFamily(fonts = RobotoBold),
                    )
                    Text(
                        text = "₹${totalPrice}",
                        style = TextStyle(
                            color = PrimaryColor,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center
                        ),
                        fontFamily = FontFamily(fonts = RobotoBold),
                    )
                }
            }
        }
    }
}
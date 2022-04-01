package com.kvr.user.ui.theme

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kvr.user.R

val MontserratRegular = listOf( Font(R.font.montserrat, FontWeight.Normal))
val MontserratBold = listOf(Font(R.font.montserrat_bold, FontWeight.Bold),)
val MontserratMedium = listOf(Font(R.font.montserrat_medium, FontWeight.Medium))
val MontserratLight = listOf(Font(R.font.montserrat_light, FontWeight.Light))

val RobotoRegular = listOf( Font(R.font.roboto, FontWeight.Normal))
val RobotoBold = listOf(Font(R.font.roboto_bold, FontWeight.Bold),)
val RobotoMedium = listOf(Font(R.font.roboto_medium, FontWeight.Medium))
val RobotoLight = listOf(Font(R.font.roboto_light, FontWeight.Light))

var SpanHeadingStyle = SpanStyle(
    color = PrimaryColor,
    fontSize = 13.sp,
    fontFamily = FontFamily(fonts = RobotoMedium)
)

var SpanContentStyle = SpanStyle(
    color = TextColor,
    fontSize = 13.sp,
    fontFamily = FontFamily(fonts = RobotoRegular)
)

var SpanContentStyleDark = SpanStyle(
    color = HeadingColor,
    fontSize = 13.sp,
    fontFamily = FontFamily(fonts = RobotoBold)
)
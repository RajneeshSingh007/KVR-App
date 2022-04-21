package com.kvr.user.screen

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.kvr.user.R
import com.kvr.user.Screen
import com.kvr.user.ui.theme.BgColor
import com.kvr.user.ui.theme.WhiteColor
import kotlinx.coroutines.delay

@OptIn(ExperimentalCoilApi::class)
@Composable
fun SplashScreen(navController: NavHostController, navigate:() -> Unit) {
    LaunchedEffect(key1 ="SplashScreen"){
        delay(2000)
        navigate()
    }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight)
            .background(color = BgColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
            Image(
                painter = rememberImagePainter(data = ""){
                    error(R.drawable.logo_big)
                    placeholder(R.drawable.logo_big)
                },
                contentDescription = "logo",
                modifier = Modifier.size(200.dp),
            )
        }
    }

}

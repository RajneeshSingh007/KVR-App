package com.kvr.user.screen.common


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.kvr.user.R
import com.kvr.user.Screen
import com.kvr.user.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun Header(navController: NavHostController? = null, title: String = "KVR", showIcon: Boolean = true, drawerClick: ()-> Unit = {}) {
    Column(modifier = Modifier .fillMaxWidth().height(56.dp).background(color = AccentColor)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(modifier = Modifier.weight(0.75f),verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.spacedBy(0.dp)) {
                IconButton(onClick = {
                    drawerClick()
                }) {
                    FaIcon(faIcon = if(showIcon)  FaIcons.Bars else FaIcons.ArrowLeft, size = 24.dp, tint = WhiteColor)
                }
                if(showIcon){
                    Image(painter = painterResource(R.drawable.header_logo), contentDescription = "header_logo",
                        modifier = Modifier.width(148.dp).height(56.dp))
                }else{
                    Text(text = title,
                        modifier = Modifier.padding(start = 8.dp),
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.White,
                            fontFamily = FontFamily(fonts = MontserratBold)
                        ),
                        maxLines = 1
                    )
                }
            }
            Row(modifier = Modifier.weight(0.25f),verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.SpaceAround) {
                IconButton(onClick = {
                    navController?.navigate(Screen.NotificationScreen.route)
                }) {
                    FaIcon(faIcon = FaIcons.BellRegular, size = 20.dp, tint = WhiteColor)
                }
                IconButton(onClick = {
                    navController?.navigate("${Screen.OrderScreen.route}/-1")
                }) {
                    FaIcon(faIcon = FaIcons.ShoppingCart, size = 20.dp, tint = WhiteColor)
                }
            }
        }
    }
}
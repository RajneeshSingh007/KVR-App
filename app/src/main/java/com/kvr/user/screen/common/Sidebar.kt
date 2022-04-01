package com.kvr.user.screen.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIconType
import com.guru.fontawesomecomposelib.FaIcons
import com.kvr.user.R
import com.kvr.user.Screen
import com.kvr.user.ui.theme.*

@Composable
fun Sidebar(navController: NavHostController,langClick: ()-> Unit = {},drawerClick: ()-> Unit = {},logoutClick: ()-> Unit = {}) {
    val items = mutableListOf<String>(stringResource(R.string.my_profile), stringResource(R.string.my_orders), stringResource(R.string.my_addresses),stringResource(R.string.search_by_vehicles),stringResource(R.string.cart),stringResource(R.string.change_language),stringResource(R.string.change_password),stringResource(R.string.log_out))
    val menuIcons = mutableListOf<FaIconType>(FaIcons.User,FaIcons.Box,FaIcons.Home,FaIcons.Search,FaIcons.ShoppingCart,FaIcons.Language,FaIcons.UserShield,FaIcons.SignOutAlt)
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .background(color = AccentColor)){
        Image(painter = painterResource(R.drawable.header_logo), contentDescription = "header_logo", modifier = Modifier
            .size(200.dp)
            .padding(start = 16.dp))
    }
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(16.dp)
        .background(color = Color.White),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column{
            items.forEachIndexed() {i, it -> SidebarItem(menuIcons[i],title = it){
                when(i){
                    0 -> {
                        drawerClick()
                        navController.navigate(Screen.ProfileScreen.route)
                    }
                    1 -> {
                        drawerClick()
                        navController.navigate("${Screen.OrderScreen.route}/1")
                    }
                    2 -> {
                        drawerClick()
                        navController.navigate(Screen.MyAddress.route)
                    }
                    3 -> {
                        drawerClick()
                        navController.navigate(Screen.SearchByVehicle.route)
                    }
                    4 -> {
                        drawerClick()
                        navController.navigate("${Screen.OrderScreen.route}/-1")
                    }
                    5 ->  langClick()
                    6 -> {
                        drawerClick()
                        navController.navigate(Screen.ResetPasswordScreen.route)
                    }
                    7 -> logoutClick()
                }
            }
            }
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically){
            Image(painter = painterResource(R.drawable.expert), contentDescription = "expert", modifier = Modifier.size(30.dp))
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            SupportButton()
        }
    }
}

@Composable
fun SidebarItem(faIcons:FaIconType = FaIcons.User,title:String = "",itemClick: ()-> Unit = {}){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)
        .clickable {
            itemClick()
        }){
        FaIcon(faIcon = faIcons, size = 20.dp, tint = GREYCOLOR)
        Spacer(modifier = Modifier.padding(horizontal = 10.dp))
        Text(
            text = title,
            style = TextStyle(
                color = HeadingColor,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            ),
            fontFamily = FontFamily(fonts = MontserratMedium),
        )
    }
}
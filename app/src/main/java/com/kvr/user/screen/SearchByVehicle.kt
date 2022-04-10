package com.kvr.user.screen

import android.icu.util.Calendar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.kvr.user.R
import com.kvr.user.Screen
import com.kvr.user.screen.common.Header
import com.kvr.user.ui.theme.*

@Composable
fun SearchByVehicle(navController: NavHostController) {
    val makerList = remember { mutableStateListOf<String>("", "Mahindra","BMW") }
    val maker = remember { mutableStateOf("") }
    val variantList = remember { mutableStateListOf<String>("", "DB4577","DB4577")}
    val variant = remember { mutableStateOf("") }
    val yearList = remember { mutableStateListOf<String>()}
    val year = remember { mutableStateOf("") }
    val partCode = remember { mutableStateOf("") }
    val partName = remember { mutableStateOf("") }

//    LaunchedEffect(key1 = "SearchByVehicle"){
//        val current = java.util.Calendar.getInstance().get(Calendar.YEAR)
//        val yearsList = (current.minus(20)..current).map {
//            it.toString()
//        }.toList()
//        yearList.addAll(yearsList)
//        year.value = "$current"
//    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(color = WhiteColor)) {
        Header(navController =navController,drawerClick = {
            navController.popBackStack()
        }, showIcon = false, title = "Search By Vehicle")
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            Text(
                text = "Size",
                style = TextStyle(
                    color = HeadingColor,
                    fontSize = 14.sp
                ),
                fontFamily = FontFamily(fonts = MontserratBold)
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
                value = maker.value,
                onValueChange = { it -> maker.value = it.filter { it.isLetterOrDigit() || it == ' '} },
                textStyle = TextStyle(
                    color = BlackColor,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(fonts = MontserratMedium),
                    textAlign = TextAlign.Start
                )
            )
            //com.kvr.user.widget.DropdownMenu(items = makerList, placeholder = ""){ i, it -> maker.value = it}
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            Text(
                text = "Engine",
                style = TextStyle(
                    color = HeadingColor,
                    fontSize = 14.sp
                ),
                fontFamily = FontFamily(fonts = MontserratBold)
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
                value = variant.value,
                onValueChange = { it -> variant.value = it.filter { it.isLetterOrDigit() || it == ' '} },
                textStyle = TextStyle(
                    color = BlackColor,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(fonts = MontserratMedium),
                    textAlign = TextAlign.Start
                )
            )
            //com.kvr.user.widget.DropdownMenu(items = variantList, placeholder = ""){ i, it -> variant.value = it}
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
//            Text(
//                text = "Select Vehicle Year",
//                style = TextStyle(
//                    color = HeadingColor,
//                    fontSize = 14.sp
//                ),
//                fontFamily = FontFamily(fonts = MontserratBold)
//            )
            //Spacer(modifier = Modifier.height(10.dp))
            //com.kvr.user.widget.DropdownMenu(items = yearList, preSelected = year.value, placeholder = ""){ i, it -> year.value = it}
            //Spacer(modifier = Modifier.padding(vertical = 10.dp))
            Text(
                text = "Part Code (Optional)",
                style = TextStyle(
                    color = HeadingColor,
                    fontSize = 14.sp
                ),
                fontFamily = FontFamily(fonts = MontserratBold)
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
                value = partCode.value,
                onValueChange = { it -> partCode.value = it.filter { it.isLetterOrDigit() || it == ' '} },
                textStyle = TextStyle(
                    color = BlackColor,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(fonts = MontserratMedium),
                    textAlign = TextAlign.Start
                )
            )
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            Text(
                text = "Part Name (Optional)",
                style = TextStyle(
                    color = HeadingColor,
                    fontSize = 14.sp
                ),
                fontFamily = FontFamily(fonts = MontserratBold)
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
                value = partName.value,
                onValueChange = { it -> partName.value = it.filter { it.isLetterOrDigit() || it == ' '} },
                textStyle = TextStyle(
                    color = BlackColor,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(fonts = MontserratMedium),
                    textAlign = TextAlign.Start
                )
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Button(
            onClick = {
                val searchQuery = "${maker.value.ifEmpty { " " }}#${variant.value.ifEmpty { " " }}#${partCode.value.ifEmpty { " " }}#${partName.value.ifEmpty { " " }}"
                navController.navigate("${Screen.ProductList.route}/Search/-1/${searchQuery}")
            },  modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(0.dp)
        ) {
            Text(
                text = "Search",
                style = TextStyle(
                    color = WhiteColor,
                    fontSize = 15.sp
                ),
                fontFamily = FontFamily(fonts = MontserratRegular),
            )
        }
    }
}
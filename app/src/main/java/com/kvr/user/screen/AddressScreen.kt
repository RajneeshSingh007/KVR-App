package com.kvr.user.screen


import android.app.Activity
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIconType
import com.guru.fontawesomecomposelib.FaIcons
import com.kvr.user.R
import com.kvr.user.Screen
import com.kvr.user.model.AddressesReq
import com.kvr.user.network.Response
import com.kvr.user.screen.common.Header
import com.kvr.user.ui.theme.*
import com.kvr.user.utils.Helpers
import com.kvr.user.viewmodel.CommonVM

@Composable
fun AddressScreen(navController: NavHostController, name:String? = "",address:String? ="",id:String? ="",loader: (show:Boolean)-> Unit = {}) {
    val context = LocalContext.current as Activity
    val commonVM = viewModel<CommonVM>()
    val commonState = commonVM.state.collectAsState().value
    val isEdit = remember { mutableStateOf(true) }
    val nameVal = remember { mutableStateOf("") }
    val lNameVal = remember { mutableStateOf("") }
    val mobileVal = remember { mutableStateOf("") }
    //val state = remember { mutableStateOf("") }
    val city = remember { mutableStateOf("") }
    val addressVal = remember { mutableStateOf("") }
    val addressVal2 = remember { mutableStateOf("") }
    val pincode = remember { mutableStateOf("") }

    LaunchedEffect(key1 = "AddressScreen"){
        //set name data
        if(name?.isNotEmpty() == true){
            val nameSp = name.split("#")
            nameVal.value = nameSp[0]
            lNameVal.value = nameSp[1]
            mobileVal.value = nameSp[2]
        }
        //set address data
        if(address?.isNotEmpty() == true){
            val addressSp = address.split("#")
            addressVal.value = addressSp[0]
            addressVal2.value = addressSp[1]
            city.value = addressSp[2]
            pincode.value = addressSp[3]
        }
    }

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
                navController.navigate(Screen.MyAddress.route){
                    popUpTo(Screen.AddressScreen.route){
                        inclusive = true
                    }
                }
            }
            else -> {
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(color = WhiteColor)) {
        Header(navController =navController,drawerClick = {
           navController.popBackStack()
        }, showIcon = false, title = if(id?.isNotEmpty() == true) "Edit Address" else "Add Address")
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        AddressItem(stringResource(R.string.first_name), nameVal.value){ it ->
            nameVal.value = it.filter { it.isLetter() || it.isWhitespace()}
        }
        AddressItem("Last Name", lNameVal.value){ it ->
            lNameVal.value = it.filter { it.isLetter() || it.isWhitespace()}
        }
        AddressItem("Mobile Number", mobileVal.value, keyboardType = KeyboardType.Number){ it ->
            val m = it.filter { it.isDigit() }
            if(m.length < 11){
                mobileVal.value = m
            }
        }
        AddressItem("Address 1", addressVal.value){
            addressVal.value = it
        }
        AddressItem("Address 2", addressVal2.value){
            addressVal2.value = it
        }
        AddressItem("City", city.value,){
            city.value = it
        }
//        AddressItem(FaIcons.Building, "State", "Delhi",Modifier.align(alignment = Alignment.CenterHorizontally),isEdit = isEdit.value, value = state.value){ it ->
//            state.value = it
//        }
        AddressItem("PinCode", pincode.value, keyboardType = KeyboardType.Number){ it ->
            val m = it.filter { it.isDigit() }
            if(m.length < 7){
                pincode.value = m
            }
        }

        Spacer(modifier = Modifier.padding(vertical = 10.dp))

        Button(
            onClick = {
                if (nameVal.value.isEmpty()) {
                    Helpers.showToast(context, 1, "Please, Enter First Name")
                }else if (lNameVal.value.isEmpty()) {
                    Helpers.showToast(context, 1, "Please, Enter Last Name")
                }else if (mobileVal.value.isEmpty()) {
                    Helpers.showToast(context, 1, "Please, Enter Phone Number")
                }else if (mobileVal.value.length < 10) {
                    Helpers.showToast(context, 1, "Please, Enter Valid Phone Number")
                }else if (addressVal.value.isEmpty()) {
                    Helpers.showToast(context, 1, "Please, Enter Address")
                }else if (addressVal2.value.isEmpty()) {
                    Helpers.showToast(context, 1, "Please, Enter Address 2")
                }else if (city.value.isEmpty()) {
                    Helpers.showToast(context, 1, "Please, Enter City")
                }else if (pincode.value.isEmpty()) {
                    Helpers.showToast(context, 1, "Please, Enter PinCode")
                }else if (pincode.value.length != 6) {
                    Helpers.showToast(context, 1, "Please, Enter Valid PinCode")
                }else{
                    commonVM.addUpdateAddressApi(AddressesReq(first_name = nameVal.value.trim(),
                        last_name = lNameVal.value.trim(),
                        mobile_no = mobileVal.value.trim(),
                        address1 = addressVal.value.trim(),
                        address2 = addressVal2.value.trim(),
                        city = city.value.trim(),
                        zipcode = pincode.value.trim(),
                        address_id = if(id?.isNotEmpty() == true) id else ""
                    ), id?.isNotEmpty() == true)
                }
            },  modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(0.dp)
        ) {
            Text(
                text = "Save Address",
                style = TextStyle(
                    color = WhiteColor,
                    fontSize = 15.sp
                ),
                fontFamily = FontFamily(fonts = MontserratRegular),
            )
        }
    }
}


@Composable
fun AddressItem(heading:String ="", value:String = "", keyboardType:KeyboardType= KeyboardType.Text,onChange: (v:String)-> Unit = {},){
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Text(
            text ="${heading}",
            style = TextStyle(
                color = TextColor,
                fontSize = 14.sp
            ),
            fontFamily = FontFamily(fonts = MontserratRegular)
        )
        Spacer(modifier = Modifier.padding(vertical = 2.dp))
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
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            value = value,
            onValueChange = { onChange(it) },
            textStyle = TextStyle(
                color = BlackColor,
                fontSize = 16.sp,
                fontFamily = FontFamily(fonts = MontserratMedium),
                textAlign = TextAlign.Start
            ),
            placeholder={
                Text(
                    text = "Enter ${heading}",
                    style = TextStyle(
                        color = Color(0xFF565a5e),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Start
                    ),
                    fontFamily = FontFamily(fonts = MontserratRegular),
                    modifier = Modifier.padding(start = 0.dp)
                )
            },
            singleLine = heading != "Address"
        )
    }
}
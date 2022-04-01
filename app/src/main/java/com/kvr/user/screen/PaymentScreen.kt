package com.kvr.user.screen

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.kvr.user.BaseApplication
import com.kvr.user.MainActivity
import com.kvr.user.R
import com.kvr.user.Screen
import com.kvr.user.model.CouponsData
import com.kvr.user.model.PaymentListerner
import com.kvr.user.model.PlaceOrderReq
import com.kvr.user.model.ProfileData
import com.kvr.user.network.Response
import com.kvr.user.screen.common.CartPriceInfo
import com.kvr.user.screen.common.Header
import com.kvr.user.ui.theme.*
import com.kvr.user.utils.Constants
import com.kvr.user.utils.Helpers
import com.kvr.user.viewmodel.CommonVM
import com.kvr.user.viewmodel.ProfileVM
import com.razorpay.Checkout
import com.razorpay.PaymentData
import org.json.JSONObject
import javax.annotation.meta.When

@Composable
fun PaymentScreen(navController: NavHostController,total:String? = "", loader: (show:Boolean)-> Unit = {}) {
    val context = LocalContext.current as MainActivity
    val commonVM = viewModel<CommonVM>()
    val commonState = commonVM.state.collectAsState().value
    val profileVM = viewModel<ProfileVM>()
    val state = profileVM.state.collectAsState().value
    val addressName = remember { mutableStateOf("") }
    val address = remember { mutableStateOf("") }
    val addressID = remember { mutableStateOf("") }
    val profileData = remember { mutableStateOf<ProfileData?>(null) }
    val payMethodSelected = remember { mutableStateOf("") }
    val couponsSelected = remember { mutableStateOf<CouponsData?>(null) }

    LaunchedEffect(key1 = "PaymentScreen"){
        profileVM.fetchProfile()
        val pref = BaseApplication.appContext.appPref
        //set address data
        val savedAddress = pref.getString(Constants.PREF_ADD).toString()
        //Log.e("savedAddress",savedAddress)
        if(savedAddress.isNotEmpty()){
            val sp = savedAddress.split("?")
            addressName.value = sp[0].replace("#", " ")
            if(sp.size > 1){
                address.value = sp[1].replace("#", " ")
            }
        }

        val savedAddressID = pref.getString(Constants.PREF_ADD_ID).toString()
        //Log.e("savedAddressID",savedAddressID)
        if(savedAddressID.isNotEmpty()){
            addressID.value = savedAddressID
        }

        //coupons
        val selectedCoupons = pref.getString(Constants.PREF_SELECTED_COUPONS).toString()
        //Log.e("selectedCoupons",selectedCoupons)
        if(selectedCoupons.isNotEmpty()){
            val csp = selectedCoupons.split("#")
            if(csp.size > 1){
                couponsSelected.value = CouponsData(id = csp[0].toInt(), title = csp[1].toString(),
                    code_name = csp[2].toString(),
                    discount = csp[3].toInt(),
                    type = csp[4].toString()
                )
            }
        }
    }

    LaunchedEffect(key1 = state){
        when(state){
            is Response.Loading ->{
            }
            is Response.Error ->{
                Helpers.showToast(context, 1, state.error)
            }
            is Response.Success ->{
                state.data?.let {
                    profileData.value = it
                }
            }
            else -> {
            }
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
                navController.navigate("${Screen.PaymentSuccess.route}/${total}")
            }
            else -> {
            }
        }
    }

    //process order api
    context.paymentListerner = object : PaymentListerner {
        override fun success(paymentID: String?, paymentData: PaymentData?) {
            val couponsId = if(couponsSelected.value != null && couponsSelected.value?.id != null){
                couponsSelected.value?.id
            }else{
                "0"
            }
            commonVM.placeOrderApi(PlaceOrderReq(payment_method = "cod", coupon_id =couponsId.toString(), shipping_address_id = addressID.value, billing_address_id = addressID.value))
        }

        override fun failure(errorCode: Int, paymentID: String?, paymentData: PaymentData?) {
            if(paymentID != null && paymentID.isNotEmpty()){
                val jObject = JSONObject(paymentID)
                val obj = jObject.optJSONObject("error")
                val description = obj?.optString("description")
                //Log.e("[Payment Data]", "${obj} ${description}")
                if(description != null && description.isNotEmpty()) {
                    Helpers.showToast(context, 1, description)
                }
            }
        }

    }

    fun processPaymentGateway(profileData: ProfileData, method:String=""){
        try {
            val options = JSONObject()
            options.put("name", context.getString(R.string.app_name))
            options.put("description", "Checkout")
            options.put("theme.color", "#3d4843");
            options.put("currency", "INR")
            //options.put("order_id", createOrderData.value.id);
            if (total != null) {
                options.put("amount", total.toDouble().times(100).toString())
            }

            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj);

            val prefill = JSONObject()
            prefill.put("email", "${profileData.email}")
            prefill.put("contact", "${profileData.phone}")
            if(method == "Paytm" || method == "Phone Pay" || method == "Google Pay"){
                prefill.put("method", "upi")
            }else if(method == "Cards"){
                prefill.put("method", "card")
            }else if(method == "Net Banking"){
                prefill.put("method", "netbanking")
            }else if(method == "Net Banking"){
                prefill.put("Wallet", "wallet")
            }
            options.put("prefill", prefill)

            val customer = JSONObject()
            customer.put("name", "${profileData.first_name} ${profileData.last_name}")
            customer.put("email", "${profileData.email}")
            customer.put("contact", "${profileData.phone}")
            options.put("customer", customer)

            val readOnly = JSONObject()
            readOnly.put("name", true)
            readOnly.put("email", true)
            readOnly.put("contact", true)
            options.put("readonly", readOnly)

            val checkout = Checkout()
            //Log.e("options", "${options.toString(4)}")
            checkout.open(context, options)
        } catch (e: Exception) {
            Helpers.showToast(context, 1, "${e.message.toString()}")
        }
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(color = WhiteColor)) {
        Header(navController =navController,drawerClick = {
            //drawerClick()
            navController.popBackStack()
        }, showIcon = false, title ="Payments")
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Text(
            text = "UPI Options:",
            style = TextStyle(
                color = HeadingColor,
                fontSize = 16.sp,
            ),
            fontFamily = FontFamily(fonts = MontserratBold),
            modifier = Modifier
                .padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        Surface( elevation = 6.dp,
            color = MaterialTheme.colors.surface,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    PaymentItems(R.drawable.paytm, "Paytm", selected = payMethodSelected.value == "Paytm"){
                        payMethodSelected.value = "Paytm"
                    }
                    PaymentItems(R.drawable.ic_phone_pe, "Phone Pay", selected = payMethodSelected.value == "Phone Pay" ){
                        payMethodSelected.value = "Phone Pay"
                    }
                    PaymentItems(R.drawable.gpay, "Google Pay", selected = payMethodSelected.value == "Google Pay"){
                        payMethodSelected.value = "Google Pay"
                    }
                }
            }
        }

        Spacer(modifier = Modifier.padding(vertical = 10.dp))

        Text(
            text = "Cards & More:",
            style = TextStyle(
                color = HeadingColor,
                fontSize = 16.sp,
            ),
            fontFamily = FontFamily(fonts = MontserratBold),
            modifier = Modifier
                .padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        Surface( elevation = 6.dp,
            color = MaterialTheme.colors.surface,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    PaymentItems(R.drawable.visamastercard, "Cards", selected = payMethodSelected.value == "Cards"){
                        payMethodSelected.value = "Cards"
                    }
                    PaymentItems(R.drawable.netbanking, "Net Banking", selected = payMethodSelected.value == "Net Banking"){
                        payMethodSelected.value = "Net Banking"
                    }
                    PaymentItems(R.drawable.wallet, "Wallet", selected = payMethodSelected.value == "Wallet"){
                        payMethodSelected.value = "Wallet"
                    }
                }
            }
        }

        Image(
            painter = painterResource(R.drawable.cardsupports),
            contentDescription = "payment_items",
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(100.dp)
                .background(color = WhiteColor)
                .alpha(0.5f)
                .align(alignment = Alignment.CenterHorizontally),
        )
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(12.dp)){
            Button(
                onClick = {
                   navController.navigate(Screen.CouponScreen.route)
                }, modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(shape = RoundedCornerShape(8.dp)),
            ) {
                Text(
                    text = "Apply Coupons",
                    style = TextStyle(
                        color = WhiteColor,
                        fontSize = 16.sp
                    ),
                    fontFamily = FontFamily(fonts = MontserratRegular),
                )
            }
        }

        total?.let { CartPriceInfo(it, "00",it,couponsSelected.value) }

        Spacer(modifier = Modifier.padding(vertical = 10.dp))

        Button(
            onClick = {
                if(payMethodSelected.value.isNotEmpty()){
                    profileData.value?.let { processPaymentGateway(profileData = it, payMethodSelected.value) }
                }else{
                    Helpers.showToast(context, 1, "Please, Select Payment Method")
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(0.dp)
        ) {
            Text(
                text = "Confirm Payment",
                style = TextStyle(
                    color = WhiteColor,
                    fontSize = 16.sp
                ),
                fontFamily = FontFamily(fonts = MontserratRegular),
            )
        }
    }
}

@Composable
fun PaymentItems(id:Int = -1, title:String="", showArrowIcon:Boolean = false, selected:Boolean = false, itemClick: ()-> Unit = {}){
    Row(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(horizontal = 10.dp, vertical = 12.dp)
        .background(color = WhiteColor), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Row(modifier = Modifier.fillMaxWidth(0.5f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Image(
                painter = painterResource(id),
                contentDescription = "payment_items",
                modifier = Modifier
                    .size(24.dp)
                    .background(color = WhiteColor)
            )
            Text(
                text =title,
                style = TextStyle(
                    color = BlackColor,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                ),
                fontFamily = FontFamily(fonts = RobotoMedium),
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(end = 4.dp)) {
            if(!showArrowIcon){
                OutlinedButton(onClick = { itemClick()},
                    modifier= Modifier.size(22.dp),
                    shape = CircleShape,
                    border= BorderStroke(2.dp, PrimaryColor),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor =  PrimaryColor)
                ) {
                    if(selected){
                        Canvas(modifier = Modifier.size(14.dp), onDraw = {
                            drawCircle(color = PrimaryColor)
                        })
                    }
                }
            }else{
                FaIcon(faIcon = FaIcons.ChevronRight, size = 24.dp, tint = PrimaryColor)
            }

        }

    }
}
package com.kvr.user.screen

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.gson.Gson
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.kvr.user.BaseApplication
import com.kvr.user.R
import com.kvr.user.Screen
import com.kvr.user.model.*
import com.kvr.user.network.Response
import com.kvr.user.screen.common.AddressInfo
import com.kvr.user.screen.common.CartPriceInfo
import com.kvr.user.screen.common.Header
import com.kvr.user.ui.theme.*
import com.kvr.user.utils.Constants
import com.kvr.user.utils.GsonExtention
import com.kvr.user.utils.Helpers
import com.kvr.user.utils.Utils
import com.kvr.user.viewmodel.CartVM
import com.kvr.user.viewmodel.CommonVM
import com.kvr.user.viewmodel.OrdersVM
import com.kvr.user.viewmodel.ProductsVM

/**
 * where screenType
 * -1 -> Cart Screen
 * 0 -> Cart Address Screen
 * 1 -> Order List Screen
 */
@Composable
fun OrderScreen(navController: NavHostController, screenType:Int? = -1,loader: (show:Boolean)-> Unit = {}) {
    val context = LocalContext.current as Activity
    val cartVM = viewModel<CartVM>()
    val state = cartVM.state.collectAsState().value
    val cartsList = remember { mutableStateListOf<CartData>() }
    val commonVM = viewModel<CommonVM>()
    val commonState = commonVM.state.collectAsState().value
    val removeIndex = remember { mutableStateOf(-1) }
    val cartType = remember { mutableStateOf(-1) }
    val addressName = remember { mutableStateOf("") }
    val address = remember { mutableStateOf("") }
    val addressID = remember { mutableStateOf("") }
    val ordersVM = viewModel<OrdersVM>()
    val orderState = ordersVM.state.collectAsState().value
    val orderList = remember { mutableStateListOf<OrderData>() }

    LaunchedEffect(key1 = "OrderScreen"){
        if(screenType == -1 || screenType == 0){
            cartVM.fetchCart()
        }
        if(screenType == 0){
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
        }
        if(screenType == 1){
            ordersVM.fetchOrdersList()
        }
    }

    //fetch cart details
    LaunchedEffect(key1 = state){
        when(state){
            is Response.Loading ->{
            }
            is Response.Error ->{
                Helpers.showToast(context, 1, state.error)
            }
            is Response.Success ->{
                state.data?.let {
                    cartsList.addAll(it.data)
                }
            }
            else -> {
            }
        }
    }

    //process cart data
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
                //based on type remove or update item in carts
                if(cartsList.isNotEmpty() && screenType == -1){
                    val cartItemAt = cartsList[removeIndex.value]
                    when (cartType.value) {
                        2 -> {
                            cartsList.remove(cartsList[removeIndex.value])
                        }
                        1 -> {
                            cartItemAt.qty = cartItemAt.qty.minus(1)
                            cartItemAt.total = cartItemAt.total.minus(cartItemAt.amount)
                            cartsList[removeIndex.value] = cartItemAt
                        }
                        0 -> {
                            cartItemAt.qty = cartItemAt.qty.plus(1)
                            cartItemAt.total = cartItemAt.total.plus(cartItemAt.amount)
                            cartsList[removeIndex.value] = cartItemAt
                        }
                    }
                    cartType.value = -1
                    removeIndex.value = -1
                }
            }
            else -> {
            }
        }
    }

    //my order data
    LaunchedEffect(key1 = orderState) {
        when(orderState){
            is Response.Loading ->{
            }
            is Response.Error ->{
                Helpers.showToast(context, 1, orderState.error)
            }
            is Response.Success ->{
                orderState.data?.let {
                    if(it.data.isNotEmpty()){
                        orderList.addAll(it.data)
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
        .background(color = WhiteColor)){
        Header(navController =navController,drawerClick = {
            navController.popBackStack()
        }, showIcon = false, title = if(screenType == 0) "Checkout" else if(screenType  == 1){
            "My Orders" }  else "Cart")
        Spacer(modifier = Modifier.padding(vertical = 10.dp))

        if(screenType  == 0){
            AddressInfo( editClick = {
              navController.navigate(Screen.MyAddress.route)
            }, title="Shipping Address:", name = addressName.value, address = address.value)
        }

        if(state.isLoading || orderState.isLoading){
            Box(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 56.dp)){
                CircularProgressIndicator()
            }
        }else if(orderList.isNotEmpty()){
            orderList.forEach {
                OrderItems(orderData = it, cartData = it.cart[0]){
                    navController.navigate("${Screen.TrackOrder.route}/${it.id}")
                }
            }
        }else if(cartsList.isNotEmpty()){
            cartsList.forEachIndexed {i, it->
                CartItems( it, showVerified = screenType == -1, showCartBtn = screenType == -1, plusClick = {
                    removeIndex.value = i
                    cartType.value = 0
                    commonVM.addUpdateCartApi(
                        CartReq(
                            cartId= it.id.toString(),
                            product_id = it.item?.id.toString(),
                        amount = it.amount.toString(),
                        discount = it.discount.toString(),
                        qty = it.qty.plus(1).toString(),
                        total = it.total.plus(it.amount).toString())
                    )
                }, minusClick = {
                    removeIndex.value = i
                    if(it.qty > 1){
                        cartType.value = 1
                        commonVM.addUpdateCartApi(
                            CartReq(product_id = it.item?.id.toString(),
                                cartId= it.id.toString(),
                                amount = it.amount.toString(),
                                discount = it.discount.toString(),
                                qty = it.qty.minus(1).toString(),
                                total = it.total.minus(it.amount).toString())
                        )
                    }else{
                        cartType.value = 2
                        //remove from cart
                        commonVM.removeCartApi(it.id)
                    }
                })
            }

            if(screenType != 1){
                CartPriceInfo(cartsList.sumOf { it.total }.toString(), "00",cartsList.sumOf { it.total }.toString())
            }
        }else{
            Spacer(modifier = Modifier.padding(vertical = 36.dp))
            Text(
                text = if(screenType == 1) "No orders found..." else "No cart items found...",
                style = TextStyle(
                    color = BlackColor,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                ),
                fontFamily = FontFamily(fonts = MontserratRegular),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.padding(vertical = 10.dp))

        Button(
            onClick = {
                if(screenType == -1){
                    navController.navigate("${Screen.OrderScreen.route}/0")
                }else if(screenType == 1){
                    navController.navigate(Screen.HomeScreen.route)
                }else{
                    if(addressID.value.isNotEmpty()){
                        val totals = cartsList.sumOf { it.total }.toString()
                        navController.navigate("${Screen.PaymentScreen.route}/${totals}")
                    }else{
                        Helpers.showToast(context, 1, "Please, Select Address")
                    }
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(0.dp)
        ) {
            Text(
                text = if(screenType  == 0){
                    "Go to Payment"
                } else if(screenType  == 1){
                    "Back to Home"
                } else {
                    "Checkout"
                },
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
fun CartItems(cartData: CartData, showVerified:Boolean = true, showCartBtn:Boolean = false, minusClick: ()-> Unit = {},plusClick: ()-> Unit = {}){
    Surface( elevation = 6.dp,
        color = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 0.dp), shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 10.dp)
                    .fillMaxHeight()) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(color = WhiteColor), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Box(modifier = Modifier.weight(0.4f)) {
                        Image(
                            painter = rememberImagePainter("${Constants.PRODUCTS_IMAGE_BASE_URL}/${cartData.item?.photo}"){
                                error(R.drawable.logo)
                             crossfade(true)
                            },
                            contentDescription = "cart_sample",
                            modifier = Modifier
                                .size(96.dp)
                                .align(alignment = Alignment.Center)
                                .background(color = WhiteColor)
                        )
                    }
                    Column(modifier = Modifier
                        .weight(0.5f)
                        .padding(vertical = 8.dp)) {
                        Text(
                            text = Helpers.removeNull(cartData.item?.name),
                            style = TextStyle(
                                color = BlackColor,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Start
                            ),
                            fontFamily = FontFamily(fonts = MontserratMedium),
                            maxLines = 2
                        )
                        Spacer(modifier = Modifier.padding(vertical = 6.dp))
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanHeadingStyle
                                ) {
                                    append("Part Number:")
                                }
                                withStyle(
                                    style = SpanContentStyle
                                ) {
                                    append(Helpers.removeNull(cartData.item?.part_no.toString()))
                                }
                            },
                        )
                        if(cartData.brand != null && cartData.brand.name.isNotEmpty()){
                            Text(
                                buildAnnotatedString {
                                    withStyle(
                                        style = SpanHeadingStyle
                                    ) {
                                        append("Brand:")
                                    }
                                    withStyle(
                                        style = SpanContentStyle
                                    ) {
                                        append(" ${cartData.brand.name}")
                                    }
                                },
                            )
                        }
                        if(cartData.supplier != null && cartData.supplier.name.isNotEmpty()) {
                            Text(
                                buildAnnotatedString {
                                    withStyle(
                                        style = SpanHeadingStyle
                                    ) {
                                        append("Supplier:")
                                    }
                                    withStyle(
                                        style = SpanContentStyle
                                    ) {
                                        append(" ${cartData.supplier.name}")
                                    }
                                },
                            )
                        }
                        Spacer(modifier = Modifier.padding(vertical = 6.dp))
                        Text(
                            text = "₹${cartData.total}",
                            style = TextStyle(
                                color = BlackColor,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            ),
                            fontFamily = FontFamily(fonts = MontserratMedium),
                        )
                    }
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(color = WhiteColor), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Row(modifier = Modifier
                        .weight(0.42f)
                        .padding(start = 4.dp)) {
                        if(showVerified){
                            FaIcon(faIcon = FaIcons.CheckCircle, size = 20.dp, tint = PrimaryColor)
                            Text(
                                text = "Verified",
                                style = TextStyle(
                                    color = PrimaryColor,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                ),
                                fontFamily = FontFamily(fonts = RobotoMedium),
                                modifier = Modifier.padding(start = 6.dp)
                            )
                        }
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.58f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = if(showCartBtn) Arrangement.Center else Arrangement.Start) {
                        Text(
                            text = "QTY:",
                            style = TextStyle(
                                color = PrimaryColor,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            ),
                            fontFamily = FontFamily(fonts = MontserratBold),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                        if(showVerified) {
                            IconButton(onClick = {minusClick() }) {
                                FaIcon(faIcon = FaIcons.MinusCircle, size = 24.dp, tint = GREYCOLOR)
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .height(30.dp)
                                    .border(
                                        1.dp,
                                        color = Color.LightGray,
                                        shape = RoundedCornerShape(4.dp)
                                    ), contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${cartData.qty}",
                                    style = TextStyle(
                                        color = PrimaryColor,
                                        fontSize = 14.sp,
                                        textAlign = TextAlign.Center
                                    ),
                                    fontFamily = FontFamily(fonts = MontserratBold),
                                )
                            }

                            IconButton(onClick = {
                                plusClick()
                            }) {
                                FaIcon(
                                    faIcon = FaIcons.PlusCircle,
                                    size = 24.dp,
                                    tint = PrimaryColor
                                )
                            }
                        }else {
                            Text(
                                text = "${cartData.qty}",
                                style = TextStyle(
                                    color = TextColor,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                ),
                                fontFamily = FontFamily(fonts = RobotoRegular),
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderItems(orderData: OrderData, cartData: CartData, itemClick: ()-> Unit = {}){
    val image = if(cartData.product_images.isNotEmpty()){
        cartData.product_images[0].photo
    }else{
        ""
    }
    val name = if(cartData.product_name.isNotEmpty()){
        cartData.product_name
    }else{
        ""
    }
    Surface( elevation = 6.dp,
        color = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 0.dp), shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 10.dp)
                    .fillMaxHeight()) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .background(color = WhiteColor), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanHeadingStyle
                            ) {
                                append("Order:")
                            }
                            withStyle(
                                style = SpanContentStyleDark
                            ) {
                                append(" ${orderData.id}")
                            }
                        },
                    )
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanHeadingStyle
                            ) {
                                append("Status:")
                            }
                            withStyle(
                                style = SpanContentStyleDark
                            ) {
                                append(" ${orderData.tracks_data[orderData.tracks_data.size-1].title}")
                            }
                        },
                    )
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .background(color = WhiteColor), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanHeadingStyle
                            ) {
                                append("Delivery By:")
                            }
                            withStyle(
                                style = SpanContentStyleDark
                            ) {
                                append(" ${orderData.id}")
                            }
                        },
                    )
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanHeadingStyle
                            ) {
                                append("Total:")
                            }
                            withStyle(
                                style = SpanContentStyleDark
                            ) {
                                append(" ₹${orderData.cart.sumOf { it.total }.toString()}")
                            }
                        },
                    )
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .background(color = WhiteColor), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanHeadingStyle
                            ) {
                                append("Placed on:")
                            }
                            withStyle(
                                style = SpanContentStyleDark
                            ) {
                                append(" ${Utils.formatDate(orderData.tracks_data[0].created_at)}")
                            }
                        },
                    )
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(color = WhiteColor), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Box(modifier = Modifier.weight(0.4f)) {
                        Image(
                            painter = rememberImagePainter("${Constants.PRODUCTS_IMAGE_BASE_URL}/${image}"){
                                error(R.drawable.logo)
                                crossfade(true)
                            },
                            contentDescription = "cart_sample",
                            modifier = Modifier
                                .size(64.dp)
                                .align(alignment = Alignment.Center)
                                .background(color = WhiteColor)
                        )
                    }
                    Column(modifier = Modifier
                        .weight(0.6f)
                        .padding(vertical = 8.dp)) {
                        Text(
                            text = Helpers.removeNull(name),
                            style = TextStyle(
                                color = BlackColor,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Start
                            ),
                            fontFamily = FontFamily(fonts = MontserratMedium),
                            maxLines = 2
                        )
                        Spacer(modifier = Modifier.padding(vertical = 6.dp))
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanHeadingStyle
                                ) {
                                    append("Part Code:")
                                }
                                withStyle(
                                    style = SpanContentStyle
                                ) {
                                    append(" 1031100")
                                }
                            },
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanHeadingStyle
                                ) {
                                    append("Brand:")
                                }
                                withStyle(
                                    style = SpanContentStyle
                                ) {
                                    append(" Brand")
                                }
                            },
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanHeadingStyle
                                ) {
                                    append("Remark:")
                                }
                                withStyle(
                                    style = SpanContentStyle
                                ) {
                                    append(" missing")
                                }
                            },
                        )
                        Spacer(modifier = Modifier.padding(vertical = 6.dp))
                        Button(
                            onClick = {
                                itemClick()
                            }, modifier = Modifier
                                .height(36.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFe1e1e1),
                                contentColor = Color(0xFFe1e1e1)
                            ),
                            shape = RoundedCornerShape(2.dp)
                        ) {
                            Text(
                                text ="Track",
                                style = TextStyle(
                                    color = PrimaryColor,
                                    fontSize = 15.sp
                                ),
                                fontFamily = FontFamily(fonts = RobotoRegular),
                            )
                        }
                    }
                }
            }
        }
    }
}


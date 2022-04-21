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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import coil.compose.rememberImagePainter
import com.kvr.user.R
import com.kvr.user.Screen
import com.kvr.user.model.AddressData
import com.kvr.user.model.CartData
import com.kvr.user.model.Tracks_data
import com.kvr.user.network.Response
import com.kvr.user.screen.common.AddressInfo
import com.kvr.user.screen.common.CartPriceInfo
import com.kvr.user.screen.common.Header
import com.kvr.user.ui.theme.*
import com.kvr.user.utils.Constants
import com.kvr.user.utils.GsonExtention
import com.kvr.user.utils.Helpers
import com.kvr.user.utils.Utils
import com.kvr.user.viewmodel.OrdersVM
import org.json.JSONException
import org.json.JSONObject

@Composable
fun TrackOrder(navController: NavHostController, id:Int? = -1, drawerClick: ()-> Unit = {}) {
    val context = LocalContext.current as Activity
    val ordersVM = viewModel<OrdersVM>()
    val orderState = ordersVM.orderstate.collectAsState().value
    val cartsList = remember { mutableStateListOf<CartData>() }
    val shippingAddress = remember { mutableStateOf<AddressData?>(null) }
    val billingAddress = remember { mutableStateOf<AddressData?>(null) }
    val shippingPriceInfo = remember { mutableStateOf(0.0) }
    val trackInfo = remember { mutableStateListOf<Tracks_data>() }

    LaunchedEffect(key1 = "TrackOrder"){
        if (id != null) {
            ordersVM.fetchOrderDetails(id)
        }
    }

    LaunchedEffect(key1 = orderState) {
        when(orderState){
            is Response.Loading ->{
            }
            is Response.Error ->{
                Helpers.showToast(context, 1, orderState.error)
            }
            is Response.Success ->{
                orderState.data?.let {
                    trackInfo.addAll(it.data.tracks_data)
                    if(it.data.shipping.isNotEmpty()){
                        try {
                            val obj = JSONObject(Helpers.stringToJSONString(it.data.shipping))
                            shippingPriceInfo.value =obj.optDouble("price",0.0)
                        }catch (e: JSONException){
                            //
                        }
                    }
                    val shippingInfo = Helpers.stringToJSONString(it.data.shipping_info)
                    shippingAddress.value = GsonExtention<AddressData>(shippingInfo)
                    //Log.e("eeO","json:: ${shippingInfo}")
                    billingAddress.value = GsonExtention<AddressData>(Helpers.stringToJSONString(it.data.billing_info))
                    //Log.e("eeO","json:: ${shippingInfo}")
                    cartsList.addAll(it.data.cart)
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
            //drawerClick()
            navController.popBackStack()
        }, showIcon = false, title = "Order ${id}")
        Spacer(modifier = Modifier.padding(vertical = 10.dp))

        if(orderState.isLoading){
            Box(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 56.dp)){
                CircularProgressIndicator()
            }
        }else{

            AddressInfo( title = "Shipping Details:", showEditBtn = false, name = "${shippingAddress.value?.first_name} ${shippingAddress.value?.last_name} - ${shippingAddress.value?.mobile_no}", address = "${shippingAddress.value?.address1}, ${shippingAddress.value?.address2}, ${shippingAddress.value?.city} - ${shippingAddress.value?.zipcode}")

            //AddressInfo( title = "Billing Address:", showEditBtn = false, name = "${billingAddress.value?.first_name} ${billingAddress.value?.last_name} - ${billingAddress.value?.mobile_no}", address = "${billingAddress.value?.address1}, ${billingAddress.value?.address2}, ${billingAddress.value?.city} - ${billingAddress.value?.zipcode}")

            Text(
                text = "Order Status:",
                style = TextStyle(
                    color = HeadingColor,
                    fontSize = 16.sp,
                ),
                fontFamily = FontFamily(fonts = MontserratBold),
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal =16.dp)
            )

            if(trackInfo.isNotEmpty()){
                OrderStatus(id, trackInfo)
            }

            Surface( elevation = 6.dp,
                color = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp), shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()) {
                        Text(
                            text = "ORDERED ITEMS",
                            style = TextStyle(
                                color = HeadingColor,
                                fontSize = 14.sp,
                            ),
                            fontFamily = FontFamily(fonts = MontserratBold),
                            modifier = Modifier
                                .padding(vertical = 12.dp, horizontal =8.dp)
                        )
                        cartsList.forEach {
                            OrdersItems(it)
                        }
                    }
                }
            }

            CartPriceInfo(cartsList.sumOf { it.total }.toString(), "${shippingPriceInfo.value}",cartsList.sumOf { it.total }.plus(shippingPriceInfo.value).toString())
        }

        Spacer(modifier = Modifier.padding(vertical = 10.dp))

        Button(
            onClick = {
                navController.navigate(Screen.HomeScreen.route)
            }, modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(0.dp)
        ) {
            Text(
                text = "Back to Home",
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
fun OrdersItems(cartData: CartData){
    val image = if(cartData.product_images.isNotEmpty()){
        cartData.product_images[0].photo
    }else{
        ""
    }
    Row(modifier= Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Column(modifier = Modifier.weight(0.5f)) {
            Text(
                text = "${Helpers.removeNull(cartData.product_name)}",
                style = TextStyle(
                    color = PrimaryColor,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Start
                ),
                fontFamily = FontFamily(fonts = MontserratMedium),
                maxLines = 1
            )
            Text(
                text = "Qty: ${cartData.qty}",
                style = TextStyle(
                    color = GREYCOLOR,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                ),
                fontFamily = FontFamily(fonts = RobotoRegular),
            )
        }
        Text(
            text = "₹${cartData.total}",
            style = TextStyle(
                color = GREYCOLOR,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            ),
            fontFamily = FontFamily(fonts = MontserratMedium),
            modifier = Modifier.weight(0.3f)
        )
        Box(
            modifier = Modifier.weight(0.2f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberImagePainter("${Constants.PRODUCTS_IMAGE_BASE_URL}/${image}"){
                    error(R.drawable.logo)
                    crossfade(true)
                },
                contentDescription = "cart_sample",
                modifier = Modifier
                    .size(40.dp)
                    .background(color = WhiteColor)
            )
        }
    }
}

@Composable
fun OrderStatus(id:Int? = -1, trackData:List<Tracks_data> ){
    val lastItem = trackData[trackData.size-1]
    Surface( elevation = 6.dp,
        color = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp), shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier= Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 12.dp, vertical = 8.dp)) {
                Row(modifier= Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .fillMaxHeight(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanHeadingStyle
                            ) {
                                append("Order ID:")
                            }
                            withStyle(
                                style = SpanContentStyle
                            ) {
                                append(" $id")
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
                                style = SpanContentStyle
                            ) {
                                append(" ${lastItem.title}")
                            }
                        },
                    )
                }

                Spacer(modifier = Modifier.padding(top = 10.dp))
                OrderStatusItem("Order placed on", showline = true, Utils.formatDate(trackData[0].created_at), PrimaryColor, PrimaryColor)
                trackData.forEachIndexed { index, tracksData ->
                    OrderStatusItem(tracksData.title,index != trackData.size -1, Utils.formatDate(tracksData.updated_at), if(index == trackData.size -1) Color(0xFFbfbfbf) else PrimaryColor, if(index == trackData.size -1) WhiteColor else  PrimaryColor)
                }
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
            }
        }
    }
}

@Composable
fun OrderStatusItem(status:String="", showline:Boolean = true, date:String="",outLinedColor:Color=Color(0xFFbfbfbf), centerColor:Color = WhiteColor){
    Row(modifier= Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp)
        .fillMaxHeight(), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Start){
        Column(verticalArrangement = Arrangement.Center){
            OutlinedButton(onClick = { },
                modifier= Modifier.size(22.dp),
                shape = CircleShape,
                border= BorderStroke(2.dp, outLinedColor),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor =  outLinedColor)
            ) {
                Canvas(modifier = Modifier.size(14.dp), onDraw = {
                    drawCircle(color = centerColor)
                })
            }
            if(showline){
                Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(start = 9.5.dp)){
                    Divider(color = outLinedColor, thickness = 2.dp, modifier = Modifier
                        .width(4.dp)
                        .height(30.dp)
                        .align(alignment = Alignment.Center)
                        .rotate(180f))
                }
            }
        }
        Box(contentAlignment = Alignment.TopStart) {
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = BlackColor,
                            fontSize = 15.sp,
                            fontFamily = FontFamily(fonts = RobotoMedium)
                        )
                    ) {
                        append(status)
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF565a5e),
                            fontSize = 13.sp,
                            fontFamily = FontFamily(fonts = RobotoRegular)
                        ),
                    ) {
                        append(" ${date}")
                    }
                },
                style = TextStyle(
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}
package com.kvr.user.screen

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.kvr.user.R
import com.kvr.user.Screen
import com.kvr.user.model.CartReq
import com.kvr.user.model.ProductDetails
import com.kvr.user.model.ProductsData
import com.kvr.user.network.Response
import com.kvr.user.screen.common.Header
import com.kvr.user.ui.theme.*
import com.kvr.user.utils.Constants
import com.kvr.user.utils.Helpers
import com.kvr.user.viewmodel.CommonVM
import com.kvr.user.viewmodel.ProductsVM

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProductDetails(navController: NavHostController, name:String ="", id:Int=-1,loader: (show:Boolean)-> Unit = {}) {
    val context = LocalContext.current as Activity
    val productsVM = viewModel<ProductsVM>()
    val state = productsVM.pstate.collectAsState().value
    val commonVM = viewModel<CommonVM>()
    val commonState = commonVM.state.collectAsState().value
    val productsDetails = remember { mutableStateOf<ProductDetails>(ProductDetails()) }

    LaunchedEffect(key1 = "ProductDetails"){
        if(id >= 0){
            productsVM.fetchProductsDetailsApiCall(id)
        }
    }

    LaunchedEffect(key1 = state){
        when(state){
            is Response.Loading ->{
            }
            is Response.Error ->{
            }
            is Response.Success ->{
                state.data?.let {
                    productsDetails.value = it
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
                navController.navigate("${Screen.OrderScreen.route}/-1")
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
            //drawerClick()
            navController.popBackStack()
        }, showIcon = false, title = name)
        if(state.isLoading){
            Box(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 56.dp)){
                CircularProgressIndicator()
            }
        }else if(productsDetails.value.data != null){
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            Text(
                text = Helpers.removeNull(productsDetails.value.data?.name),
                style = TextStyle(
                    color = PrimaryColor,
                    fontSize = 16.sp,
                ),
                fontFamily = FontFamily(fonts = MontserratBold),
                modifier = Modifier
                    .padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            Column {
                Surface( elevation = 4.dp,
                    color = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .size(150.dp)
                            .padding(start = 0.dp), shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .background(color = WhiteColor),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = rememberImagePainter("${Constants.PRODUCTS_IMAGE_BASE_URL}/${Helpers.removeNull(productsDetails.value.data?.photo)}"){
                                    crossfade(true)
                                }, contentDescription = "brand", modifier = Modifier
                                    .size(150.dp)
                                    .background(color = WhiteColor)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                Column(Modifier.padding(start = 16.dp)) {
                    Text(
                        text = Helpers.removeNull(name),
                        style = TextStyle(
                            color = HeadingColor,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        ),
                        fontFamily = FontFamily(fonts = MontserratBold),
                    )
                    Spacer(modifier = Modifier.padding(vertical = 6.dp))
                    Text(
                        text = "₹${productsDetails.value.data?.price}",
                        style = TextStyle(
                            color = BlackColor,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        ),
                        fontFamily = FontFamily(fonts = MontserratBold),
                    )
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                    Row( Modifier.padding(start = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                        Image(
                            painter = painterResource(R.drawable.delivery),
                            contentDescription = "delivery",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = stringResource(R.string.delivery_text),
                            style = TextStyle(
                                color = TextColor,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            ),
                            fontFamily = FontFamily(fonts = MontserratRegular),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                    Text(
                        text = stringResource(R.string.details),
                        style = TextStyle(
                            color = BlackColor,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center
                        ),
                        fontFamily = FontFamily(fonts = MontserratBold),
                    )
                    Spacer(modifier = Modifier.padding(vertical = 6.dp))
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanHeadingStyle
                            ) {
                                append(stringResource(R.string.part_codes))
                            }
                            withStyle(
                                style = SpanContentStyle
                            ) {
                                append(Helpers.removeNull(productsDetails.value.data?.part_code))
                            }
                        },
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanHeadingStyle
                            ) {
                                append(stringResource(R.string.part_names))
                            }
                            withStyle(
                                style = SpanContentStyle
                            ) {
                                append(Helpers.removeNull(name))
                            }
                        },
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanHeadingStyle
                            ) {
                                append(stringResource(R.string.remarks))
                            }
                            withStyle(
                                style = SpanContentStyle
                            ) {
                                append(Helpers.removeNull(productsDetails.value.data?.remark))
                            }
                        },
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanHeadingStyle
                            ) {
                                append(stringResource(R.string.desc))
                            }
                            withStyle(
                                style = SpanContentStyle
                            ) {
                                append(Helpers.removeNull(productsDetails.value.data?.sort_details))
                            }
                        },
                        modifier = Modifier.padding(end = 10.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                Text(
                    text = stringResource(R.string.more_product),
                    style = TextStyle(
                        color = HeadingColor,
                        fontSize = 16.sp,
                    ),
                    fontFamily = FontFamily(fonts = MontserratBold),
                    modifier = Modifier
                        .padding(start = 16.dp)
                )
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                Image(
                    painter = rememberImagePainter("${Constants.PRODUCTS_IMAGE_BASE_URL}/${Helpers.removeNull(productsDetails.value.data?.brand?.photo)}"){
                      crossfade(true)
                    },
                    contentDescription = "more_brand_sample",
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(100.dp)
                        .background(color = WhiteColor)
                        .align(alignment = Alignment.CenterHorizontally),
                )
                Spacer(modifier = Modifier.padding(vertical = 8.dp))
                Text(
                    text = stringResource(R.string.specs),
                    style = TextStyle(
                        color = HeadingColor,
                        fontSize = 16.sp,
                    ),
                    fontFamily = FontFamily(fonts = MontserratBold),
                    modifier = Modifier
                        .padding(start = 16.dp)
                )
                Spacer(modifier = Modifier.padding(vertical = 8.dp))
                if(productsDetails.value.data?.specification_name?.isNotEmpty() == true){
                    val specifDesc = Helpers.stringToStringArray(productsDetails.value.data?.specification_description)
                    val specif = Helpers.stringToStringArray(productsDetails.value.data?.specification_name)
                    specif.forEachIndexed {i, it ->
                        SpecificationItems(specif[i],specifDesc[i],if(i+1 < specif.size) specif[i+1] else "",if(i+1 < specif.size) specifDesc[i+1] else "")
                    }
                }
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                Image(
                    painter = painterResource(R.drawable.genuine),
                    contentDescription = "genuine",
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(100.dp)
                        .background(color = WhiteColor)
                        .alpha(0.5f)
                        .align(alignment = Alignment.CenterHorizontally),
                )

                Spacer(modifier = Modifier.padding(vertical = 10.dp))

                Button(
                    onClick = {
                        //add to cart
                        commonVM.addUpdateCartApi(CartReq(product_id = productsDetails.value.data?.id.toString(),
                            amount = productsDetails.value.data?.previous_price.toString(),
                            discount = productsDetails.value.data?.discount_price.toString(),
                            qty = "1",
                            total = productsDetails.value.data?.price.toString()))
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Text(
                        text = stringResource(R.string.buy_now),
                        style = TextStyle(
                            color = WhiteColor,
                            fontSize = 16.sp
                        ),
                        fontFamily = FontFamily(fonts = MontserratRegular),
                    )
                }
            }
        }
    }
}

@Composable
fun SpecificationItems(heading1:String="", content1:String="", heading2: String="", content2:String=""){
    Row(Modifier.padding(start = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanHeadingStyle
                ) {
                    append(if(heading1.isNotEmpty()) "${heading1}:" else "")
                }
                withStyle(
                    style = SpanContentStyle
                ) {
                    append(" ${content1.replace("/",",")}")
                }
            },
            modifier = Modifier.weight(0.5f).padding(end = 4.dp)
        )
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanHeadingStyle
                ) {
                    append(if(heading2.isNotEmpty()) "${heading1}:" else "")
                }
                withStyle(
                    style = SpanContentStyle
                ) {
                    append(" ${content2.replace("/",",")}")
                }
            },
            modifier = Modifier.weight(0.5f).padding(end = 4.dp)
        )
    }
    Spacer(modifier = Modifier.padding(vertical = 6.dp))
}
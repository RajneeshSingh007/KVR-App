package com.kvr.user.screen

import android.app.Activity
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColor
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.palette.graphics.Palette
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.kvr.user.R
import com.kvr.user.Screen
import com.kvr.user.model.BannerData
import com.kvr.user.model.Brandsdata
import com.kvr.user.model.ProductsData
import com.kvr.user.network.Response
import com.kvr.user.screen.common.Header
import com.kvr.user.screen.common.SupportButton
import com.kvr.user.ui.theme.*
import com.kvr.user.utils.Constants
import com.kvr.user.utils.Helpers
import com.kvr.user.viewmodel.HomeVM
import com.kvr.user.viewmodel.ProductsVM
import com.kvr.user.widget.CompleteSearchView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.time.Duration

@OptIn(ExperimentalPagerApi::class, kotlin.time.ExperimentalTime::class)
@Composable
fun HomeScreen(navController: NavHostController,drawerClick: ()-> Unit = {}) {
    val context = LocalContext.current as Activity
    val homeVM = viewModel<HomeVM>()
    val state = homeVM.state.collectAsState().value
    val bannerState = homeVM.bstate.collectAsState().value
    val bannerStatePager = rememberPagerState()
    val pstate = homeVM.pstate.collectAsState().value
    val productsPopularList = remember { mutableStateListOf<ProductsData>() }
    val productsFlashList = remember { mutableStateListOf<ProductsData>() }
    val productsSearchList = remember { mutableStateListOf<ProductsData>() }
    val bannerList = remember { mutableStateListOf<BannerData>() }
    val brandList = remember { mutableStateListOf<Brandsdata>() }

    LaunchedEffect(key1 = "HomeScreen"){
        delay(200)
        homeVM.fetchBrands()
        homeVM.fetchProductsType(1, "popular")
        homeVM.fetchProductsType(2, "flash_deal")
        homeVM.fetchBanners()
    }


    LaunchedEffect(key1 = pstate){
        when(pstate){
            is Response.Success ->{
                pstate.data?.let {
                    when(it.type) {
                        1 -> productsPopularList.addAll(it.data)
                        2 -> productsFlashList.addAll(it.data)
                        3 -> {
                            productsSearchList.clear()
                            productsSearchList.addAll(it.data)
                        }
                        else -> {}
                    }
                }
            }
            else -> {
            }
        }
    }

    //brand list
    LaunchedEffect(key1 = state){
        when(state){
            is Response.Success ->{
                state.data?.let { brandList.addAll(it.data) }
            }
            else -> {
            }
        }
    }

    //banner
    LaunchedEffect(key1 = bannerState){
        when(bannerState){
            is Response.Success ->{
                bannerState.data?.let { bannerList.addAll(it.data) }
            }
            else -> {
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(color = BgColor)){
        Header(navController =navController,drawerClick = {
            drawerClick()
        })
        Row(
            Modifier
                .background(color = AccentColor)
                .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 8.dp)){

            CompleteSearchView(productsSearchList.map { it.name },
                loading = pstate.isLoading,
                searchCallback = {
                     homeVM.searchProducts(it)
                },
                selectedItemData = {
                i, search ->
                    val item = productsSearchList.find { it.name.equals(search, ignoreCase = true) }
                    if(item != null){
                        navController.navigate("${Screen.ProductDetails.route}/${item.name}/${item.id}")
                        productsSearchList.clear()
                    }
            })
        }
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Text(
            text = stringResource(R.string.home_heading1),
            style = TextStyle(
                color = HeadingColor,
                fontSize = 16.sp,
            ),
            fontFamily = FontFamily(fonts = MontserratBold),
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp)
        )
        if(state.isLoading){
            Box(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 36.dp)){
                CircularProgressIndicator()
            }
        }else if(brandList.isNotEmpty()){
            brandList.forEachIndexed {i,item->
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                Brands(Color(if(i%2 == 0) 0xFF007842 else 0xFFf78122),"${Constants.BRANDS_IMAGE_BASE_URL}${item.photo}", item.name){
                    navController.navigate("${Screen.ProductList.route}/${item.name}/${item.id}/-1")
                }
            }
        }

        Spacer(modifier = Modifier.padding(vertical = 16.dp))

        HorizontalPager( count = bannerList.size,state = bannerStatePager,contentPadding = PaddingValues(horizontal = 32.dp),
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            bannerList.forEach { j ->
                Banners(j)
            }
        }

        HorizontalPagerIndicator(
            pagerState = bannerStatePager,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            activeColor = PrimaryColor,
            inactiveColor = Color(0xFFE0DFDF),
            indicatorShape = CircleShape,
            spacing = 4.dp,

        )
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Text(
            text = stringResource(R.string.home_heading2),
            style = TextStyle(
                color = HeadingColor,
                fontSize = 16.sp,
            ),
            fontFamily = FontFamily(fonts = MontserratBold),
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        if(pstate.isLoading){
            Box(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 36.dp)){
                CircularProgressIndicator()
            }
        }else if(productsPopularList.isNotEmpty()){
            Products(productsPopularList){
                navController.navigate("${Screen.ProductDetails.route}/${it.name}/${it.id}")
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Text(
            text = stringResource(R.string.home_heading3),
            style = TextStyle(
                color = HeadingColor,
                fontSize = 16.sp,
            ),
            fontFamily = FontFamily(fonts = MontserratBold),
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Button(
            onClick = {

            }, modifier = Modifier
                .fillMaxWidth(0.3f)
                .height(30.dp)
                .align(alignment = Alignment.CenterHorizontally)
                .clip(shape = RoundedCornerShape(8.dp)),
        ) {
            FaIcon(faIcon = FaIcons.Clock, size = 16.dp, tint = Color.White, modifier = Modifier.padding(end = 8.dp))
            Text(
                text = stringResource(R.string.deal_times),
                style = TextStyle(
                    color = WhiteColor,
                    fontSize = 14.sp
                ),
                fontFamily = FontFamily(fonts = MontserratMedium),
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        if(pstate.isLoading){
            Box(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 36.dp)){
                CircularProgressIndicator()
            }
        }else if(productsFlashList.isNotEmpty()){
            DealsCard(productsFlashList){
                navController.navigate("${Screen.ProductDetails.route}/${it.name}/${it.id}")
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        SupportButton(widthFraction = 0.5f,modifier = Modifier
            .align(alignment = Alignment.CenterHorizontally)
            .padding(vertical = 8.dp))
        Spacer(modifier = Modifier.padding(vertical = 16.dp))
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun Brands(color: Color  = PrimaryColor, imageUrl:String="", name:String = "",itemClick: ()-> Unit = {}){
    val painter = rememberImagePainter(
        data = imageUrl,
        builder = {
            crossfade(true)
            allowHardware(false)
        }
    )
    val colors = remember { mutableStateOf(color.toArgb()) }
    val painterState = painter.state
    if (painterState is ImagePainter.State.Success) {
        LaunchedEffect(key1 = painter) {
            launch {
                val result = painter.imageLoader.execute(painter.request).drawable
                val bitmap = (result as BitmapDrawable).bitmap
                colors.value = Palette.from(bitmap)
                    .generate()
                    .getVibrantColor(color.toArgb())

            }
        }
    }
    Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
        Spacer(modifier = Modifier.weight(0.05f))
        Box(modifier = Modifier
            .weight(0.9f)
            .clickable {
                itemClick()
            }
            .border(width = 2.dp, color = Color(colors.value), shape = RoundedCornerShape(4.dp))
            .fillMaxWidth(1.0f)
            .size(200.dp)
            .align(alignment = Alignment.CenterVertically)) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .background(color = WhiteColor), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Image(painter = painter, contentDescription = "brand", modifier = Modifier
                    .size(200.dp)
                    .background(color = WhiteColor))
            }
            Box( modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(40.dp)
                .background(color = Color.Transparent)
                .align(alignment = Alignment.BottomEnd)
                .clip(shape = CutCornerShape(topStartPercent = 96, topEndPercent = 0))) {
                Button(
                    onClick = {

                    }, modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(colors.value),
                        contentColor = WhiteColor
                    )
                ) {
                    Text(
                        text = name,
                        style = TextStyle(
                            color = WhiteColor,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Right
                        ),
                        fontFamily = FontFamily(fonts = MontserratBold),
                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(0.05f))
    }
}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun Banners(bannerData: BannerData) {
    Card(
        backgroundColor = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .height(164.dp)
            .padding(start = 16.dp), shape = RoundedCornerShape(4.dp)
    ) {
        Image(
            painter = rememberImagePainter("${bannerData.photo}"){
                error(R.drawable.logo)
                placeholder(R.drawable.logo)
            },
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(164.dp),
            contentScale = ContentScale.FillBounds
        )
    }
}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun Products(list: List<ProductsData>,itemClick: (p:ProductsData)-> Unit = {}){
    val itemSize: Dp = LocalConfiguration.current.screenWidthDp.dp / 6
    FlowRow(mainAxisSize = SizeMode.Expand, mainAxisSpacing= 0.dp, mainAxisAlignment = FlowMainAxisAlignment.SpaceEvenly) {
        list.forEachIndexed { _, it ->
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .clickable {
                    itemClick(it)
                }
                .size(itemSize + 48.dp)) {
                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = PrimaryColor,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .fillMaxWidth(0.8f)
                        .size(70.dp)
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
                            painter = rememberImagePainter("${Constants.PRODUCTS_IMAGE_BASE_URL}/${it.photo}"){
                             crossfade(true)
                            }, contentDescription = "products", modifier = Modifier
                                .size(48.dp)
                                .background(color = WhiteColor)
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                Text(
                    text = Helpers.removeNull(it.name),
                    style = TextStyle(
                        color = HeadingColor,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    ),
                    fontFamily = FontFamily(fonts = MontserratMedium),
                    maxLines = 1
                )
                Text(
                    text = "₹${it.previous_price.toString()}",
                    style = TextStyle(
                        color = PrimaryColor,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    ),
                    fontFamily = FontFamily(fonts = MontserratMedium),
                )
                Spacer(modifier = Modifier.padding(vertical = 6.dp))
            }
        }
    }
}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun DealsCard(list: List<ProductsData>,itemClick: (p:ProductsData)-> Unit = {}){
    val itemSize: Dp = LocalConfiguration.current.screenWidthDp.dp / 2
    FlowRow(mainAxisSize = SizeMode.Expand, mainAxisSpacing= 0.dp, mainAxisAlignment = FlowMainAxisAlignment.SpaceEvenly) {
        list.forEachIndexed { _, it ->
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .clickable {
                    itemClick(it)
                }
                .size(itemSize)) {
                Surface( elevation = 4.dp,
                    color = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(8.dp)) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .size(120.dp)
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
                                painter = rememberImagePainter("${Constants.PRODUCTS_IMAGE_BASE_URL}/${it.thumbnail}"){
                                    crossfade(true)
                                }, contentDescription = "products", modifier = Modifier
                                    .background(color = WhiteColor)
                                    .height(96.dp),
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(vertical = 6.dp))
                Text(
                    text = Helpers.removeNull(it.name),
                    style = TextStyle(
                        color = HeadingColor,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    ),
                    fontFamily = FontFamily(fonts = MontserratMedium),
                    maxLines = 1
                )
                Text(
                    text = "₹${it.previous_price.toString()}",
                    style = TextStyle(
                        color = PrimaryColor,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    ),
                    fontFamily = FontFamily(fonts = MontserratMedium),
                )
            }
        }
    }
}
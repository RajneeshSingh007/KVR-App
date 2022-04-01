package com.kvr.user.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.kvr.user.R
import com.kvr.user.Screen
import com.kvr.user.model.Brandsdata
import com.kvr.user.model.Category
import com.kvr.user.model.ProductsData
import com.kvr.user.network.Response
import com.kvr.user.screen.common.Header
import com.kvr.user.screen.common.SupportButton
import com.kvr.user.ui.theme.*
import com.kvr.user.utils.Constants
import com.kvr.user.utils.Helpers
import com.kvr.user.viewmodel.HomeVM
import com.kvr.user.viewmodel.ProductsVM
import com.kvr.user.widget.Border
import com.kvr.user.widget.Borders
import com.kvr.user.widget.border
import kotlinx.coroutines.launch

@Composable
fun ProductList(navController: NavHostController, name:String ="", id:Int=-1, drawerClick: ()-> Unit = {}) {
    val productsVM = viewModel<ProductsVM>()
    val state = productsVM.state.collectAsState().value
    val productsList = remember { mutableStateListOf<ProductsData>() }
    val catDialog = remember{mutableStateOf(false)}
    val cstate = productsVM.cstate.collectAsState().value
    val catList = remember { mutableStateListOf<Category>() }

    val borderColor = GREYCOLOR
    val borders = Borders(bottom = Border(0.7.dp, borderColor), top = Border(0.7.dp, Color.Transparent), start = Border(0.7.dp, borderColor), end = Border(0.7.dp, Color.Transparent))
    val headersBorders = Borders(bottom = Border(0.7.dp, borderColor), top = Border(0.7.dp, borderColor), start = Border(0.7.dp, borderColor), end = Border(0.7.dp, Color.Transparent))
    val lastRowItemBorders = Borders(bottom = Border(0.7.dp, Color.Transparent), top = Border(0.7.dp, borderColor), start = Border(0.7.dp, borderColor), end = Border(0.7.dp, borderColor))
    val lastItemBorders = Borders(bottom = Border(0.7.dp, borderColor), top = Border(0.7.dp, borderColor), start = Border(0.7.dp, borderColor), end = Border(0.7.dp, borderColor))

    LaunchedEffect(key1 = "ProductList"){
        if(id >= 0){
            productsVM.fetchProductsApiCall(id)
        }
        productsVM.fetchCategoryListApiCall()
    }

    LaunchedEffect(key1 = state){
        when(state){
            is Response.Success ->{
                state.data?.let {
                    if(it.data.products.isNotEmpty()) {
                        productsList.addAll(it.data.products)
                    }
                }
            }
            else -> {
            }
        }
    }

    LaunchedEffect(key1 = cstate){
        when(cstate){
            is Response.Success ->{
                cstate.data?.let {
                    if(it.data.isNotEmpty()) {
                        catList.addAll(it.data)
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
        }, showIcon = false, title = name)
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        CategoryList(catList, catDialog.value, btnClick = {
            catDialog.value = true
        }, onDismiss = {
            it,item ->
            catDialog.value = false
            if(it){
                if (item != null) {
                    productsList.filter { it.category_id == item.id }
                }
            }
        })
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        //header
        Row(
            Modifier
                .padding(horizontal = 4.dp)
                .background(color = Color(0xFFe1e1e1))
                .fillMaxWidth()
                .height(56.dp)) {
            TableCell(true,text = "DIN", weight = .1f,true,borders =headersBorders)
            TableCell(true,text = "Part No", weight = .1f,true,borders =headersBorders)
            TableCell(true,text = "Part Code", weight = .2f,true,borders =headersBorders)
            TableCell(true,text = "Part Name", weight = .2f,true,borders =headersBorders)
            TableCell(true,text = "Remarks", weight = .2f,true,borders =headersBorders)
            TableCell(true,text = "Image", weight = .2f,true,borders = if(productsList.isEmpty()) lastItemBorders else lastRowItemBorders)
        }
        if(state.isLoading){
            Box(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 56.dp)){
                CircularProgressIndicator()
            }
        }else if(productsList.isNotEmpty()) {
            Column(modifier = Modifier.padding(horizontal = 4.dp)) {
                productsList.forEachIndexed{i,it ->
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("${Screen.ProductDetails.route}/${it.name}/${it.id}")
                        }
                        .height(64.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        TableCell(text = "${i +1 }", weight = .1f,borders =borders)
                        TableCell(text = "${it.part_no}", weight = .1f,borders =borders)
                        TableCell(text = Helpers.removeNull(it.part_code), weight = .2f,borders =borders)
                        TableCell(text = Helpers.removeNull(it.name), weight = .2f,borders =borders)
                        TableCell(text = Helpers.removeNull(it.remark), weight = .2f,borders =borders)
                        TableCell( weight = .2f, imageUrl = "${Constants.PRODUCTS_IMAGE_BASE_URL}/${it.photo}", isImage = true,borders =if(i == productsList.size -1) lastItemBorders else lastRowItemBorders)
                    }
                }
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
fun RowScope.TableCell(
    isHeader:Boolean = false,
    text: String = "",
    weight: Float = 0.2f,
    isBold:Boolean = false,
    isImage:Boolean = false,
    borders: Borders,
    imageUrl:String = ""
) {

    Box(modifier = Modifier
        .fillMaxHeight()
        .fillMaxHeight()
        .weight(weight)
        .border(
            bottom = borders.bottom,
            end = borders.end,
            start = borders.start,
            top = borders.top
        )
    ) {
        if(isImage){
            Image(
                painter = rememberImagePainter(imageUrl){crossfade(true)},
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .align(alignment = Alignment.Center),
            )
        }else{
            Text(
                text = text,
                style = TextStyle(
                    color = if(isHeader) PrimaryColor else BlackColor,
                    fontSize = if(isHeader) 11.sp else 10.sp,
                ),
                fontFamily = if(isBold) FontFamily(fonts =  RobotoMedium) else FontFamily(fonts =  RobotoRegular),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .align(alignment = Alignment.Center)
                    .padding(8.dp),
                maxLines = 1
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, coil.annotation.ExperimentalCoilApi::class)
@Composable
fun CategoryList(itemList:List<Category>, showDialog: Boolean,btnClick: () -> Unit, onDismiss: (isChanged:Boolean, category:Category?)->Unit){
    Box(modifier = Modifier.padding(horizontal = 16.dp)){
        Button(
            onClick = {
                btnClick()
            }, modifier = Modifier
                .fillMaxWidth(0.42f)
                .height(36.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFe1e1e1),
                contentColor = Color(0xFFe1e1e1)
            ),
            shape = RoundedCornerShape(2.dp)

        ) {
            Text(
                text = stringResource(R.string.change_category),
                style = TextStyle(
                    color = PrimaryColor,
                    fontSize = 15.sp
                ),
                fontFamily = FontFamily(fonts = RobotoRegular),
            )
        }
    }
    if(showDialog){
        Dialog(
            onDismissRequest = { onDismiss(false,null) },
            DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        ) {
            Box(
                contentAlignment= Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.change_category),
                        style = TextStyle(
                            color = HeadingColor,
                            fontSize = 16.sp,
                        ),
                        fontFamily = FontFamily(fonts = MontserratBold),
                        modifier = Modifier
                            .padding(start = 16.dp,top=16.dp)
                    )
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                    LazyColumn {
                        items(itemList) {
                            ListItem(
                                text = { Text(Helpers.removeNull(it.name))},
                                icon ={ Image(
                                    painter = rememberImagePainter("${Constants.CATEGORY_IMAGE_BASE_URL}/${it.photo}"){crossfade(true)},
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp),
                                )},
                                modifier = Modifier.clickable {
                                    onDismiss(true,it)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
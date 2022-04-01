package com.kvr.user.screen

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIconType
import com.guru.fontawesomecomposelib.FaIcons
import com.kvr.user.R
import com.kvr.user.Screen
import com.kvr.user.model.ProfileData
import com.kvr.user.model.ProfileReq
import com.kvr.user.network.Response
import com.kvr.user.screen.common.Header
import com.kvr.user.ui.theme.*
import com.kvr.user.utils.Constants
import com.kvr.user.utils.Helpers
import com.kvr.user.utils.ImagePathUtil
import com.kvr.user.viewmodel.CommonVM
import com.kvr.user.viewmodel.ProfileVM
import java.io.File

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProfileScreen(navController: NavHostController,loader: (show:Boolean)-> Unit = {}) {
    val context = LocalContext.current as Activity
    val profileVM = viewModel<ProfileVM>()
    val state = profileVM.state.collectAsState().value
    val commonVM = viewModel<CommonVM>()
    val commonState = commonVM.state.collectAsState().value
    val isEdit = remember { mutableStateOf(false) }
    val name = remember { mutableStateOf("") }
    val lname = remember { mutableStateOf("") }
    val phone = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val address = remember { mutableStateOf("") }
    val userImage = remember { mutableStateOf<Any?>("") }
    val file = remember { mutableStateOf<File?>(null) }

    LaunchedEffect(key1 = "ProfileScreen"){
        profileVM.fetchProfile()
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
                isEdit.value = false
            }
            else -> {
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
                    if(it.photo != null && it.photo.length > 0 && it.photo.isNotEmpty()){
                        userImage.value = "${Constants.CATEGORY_IMAGE_BASE_URL}/${it.photo}"
                    }
                    name.value = it.first_name
                    lname.value = it.last_name
                    phone.value = it.phone
                    email.value = it.email
                }
            }
            else -> {
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        userImage.value = uri
        val getPath = ImagePathUtil.getFilePathByUri(context, uri as Uri).toString()
        if (getPath.isNotEmpty() && getPath != "null") {
            file.value = File(getPath.toString())
        }
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(color = WhiteColor)) {
        Header(navController =navController,drawerClick = {
            navController.popBackStack()
        }, showIcon = false, title = "My Profile")
        Box(modifier= Modifier
            .background(color = AccentColor)
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center){
            Column {
                Image(
                    painter = rememberImagePainter(data = userImage.value, builder = {
                        placeholder(R.drawable.profile_image)
                        error(R.drawable.profile_image)
                        transformations(CircleCropTransformation())
                    }),
                    contentDescription = "profile_image",
                    modifier = Modifier
                        .size(120.dp)
                        .clickable {
                            if (isEdit.value) {
                                launcher.launch("image/*")
                            }
                        }.clip(CircleShape),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                Text(
                    text = "${name.value} ${lname.value}",
                    style = TextStyle(
                        color = WhiteColor,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    ),
                    fontFamily = FontFamily(fonts = MontserratBold),
                )
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        if(state.isLoading){
            Box(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 56.dp)){
                CircularProgressIndicator()
            }
        }else{
            ProfileInfo(FaIcons.UserCircle, "First Name", name.value, Modifier.align(alignment = Alignment.CenterHorizontally),isEdit = isEdit.value, value = name.value){ it ->
                name.value = it.filter { it.isLetter() }
            }
            ProfileInfo(FaIcons.UserCircle, "Last Name", lname.value, Modifier.align(alignment = Alignment.CenterHorizontally),isEdit = isEdit.value, value = lname.value){ it ->
                lname.value = it.filter { it.isLetter() }
            }
            ProfileInfo(FaIcons.Phone, "Phone", phone.value,Modifier.align(alignment = Alignment.CenterHorizontally),isEdit = isEdit.value, value = phone.value, keyboardType = KeyboardType.Number){ it ->
                val m = it.filter { it.isDigit() }
                if(m.length < 11){
                    phone.value = m
                }
            }
            ProfileInfo(FaIcons.Envelope, "Email", email.value,Modifier.align(alignment = Alignment.CenterHorizontally),isEdit = isEdit.value, value = email.value, keyboardType = KeyboardType.Email){
                email.value = it
            }
            //      ProfileInfo(FaIcons.Home, "Address", "54, Charu Villas, Hadapsar Udaipur - 197796",Modifier.align(alignment = Alignment.CenterHorizontally),isEdit = isEdit.value, value = address.value){
            //           address.value = it
            //       }

            Spacer(modifier = Modifier.padding(vertical = 10.dp))
        }

        Button(
            onClick = {
                if(!isEdit.value){
                    isEdit.value = true
                }else{
                    commonVM.updateProfile(ProfileReq(first_name = name.value, last_name = lname.value, phone = phone.value, email = email.value),file.value)
                }
            }, modifier = Modifier
                .fillMaxWidth(if (!isEdit.value) 0.5f else 1.0f)
                .height(if (!isEdit.value) 40.dp else 50.dp)
                .align(alignment = Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if(!isEdit.value)  Color(0xFFe1e1e1) else PrimaryColor,
                contentColor = if(!isEdit.value)  Color(0xFFe1e1e1) else PrimaryColor
            ),
            shape = RoundedCornerShape(if(!isEdit.value) 2.dp else 0.dp)
        ) {
            Text(
                text = if(!isEdit.value) "Edit Profile" else "Save Profile",
                style = TextStyle(
                    color = if(!isEdit.value) PrimaryColor else WhiteColor,
                    fontSize = 15.sp
                ),
                fontFamily = FontFamily(fonts = MontserratRegular),
            )
        }
        if(!isEdit.value){
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
        }
    }
}

@Composable
fun ProfileInfo(faIconType: FaIconType= FaIcons.UserCircle, heading:String ="", content:String="", modifier: Modifier, isEdit:Boolean = false, value:String = "", keyboardType:KeyboardType= KeyboardType.Text,onChange: (v:String)-> Unit = {},){
    Surface( elevation = 4.dp,
        color = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .then(modifier),
    ) {
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 1.dp),) {
                    FaIcon(faIcon = faIconType, size = 24.dp, tint = PrimaryColor)
                    Text(
                        text ="${heading}:",
                        style = TextStyle(
                            color = HeadingColor,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Start
                        ),
                        fontFamily = FontFamily(fonts = MontserratBold),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp, vertical = if (isEdit) 10.dp else 1.dp),) {
                    if(isEdit){
                        TextField(
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(4.dp))
                                .background(color = Color(0xFFf3f3f3))
                                .weight(1.0f)
                                .height(if (heading != "Address") 50.dp else 100.dp),
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
                    }else{
                        Spacer(modifier = Modifier.weight(0.15f))
                        Text(
                            text = content,
                            style = TextStyle(
                                color = Color(0xFF565a5e),
                                fontSize = 15.sp,
                                textAlign = TextAlign.Start
                            ),
                            fontFamily = FontFamily(fonts = MontserratBold),
                            modifier = Modifier.weight(0.85f)
                        )
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.padding(vertical = 10.dp))
}
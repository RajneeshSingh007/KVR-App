package com.kvr.user.widget


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import coil.compose.rememberImagePainter
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.kvr.user.R
import com.kvr.user.ui.theme.*
import com.kvr.user.utils.Constants
import com.kvr.user.utils.Helpers
import java.util.*
import kotlin.collections.ArrayList

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CompleteSearchView(items: List<String> = ArrayList<String>(0), loading: Boolean = false, searchCallback:(v:String) -> Unit, selectedItemData:(pos:Int, value:String) -> Unit) {
    var textfieldSize by remember { mutableStateOf(Size.Zero) }
    var search by remember { mutableStateOf("") }
    var exp by remember { mutableStateOf(items.isNotEmpty()) }

    LaunchedEffect(key1 = search){
        if(search.length >= 2) {
            searchCallback(search)
        }
    }

    ExposedDropdownMenuBox(expanded = exp, onExpandedChange = {
        exp = !exp
    }){

        Surface(elevation = 4.dp,
            color = MaterialTheme.colors.surface,
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth().onGloballyPositioned { textfieldSize = it.size.toSize() }){
            TextField(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(4.dp))
                    .background(color = WhiteColor)
                    .fillMaxHeight()
                    .fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = WhiteColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.Black
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                value = search,
                onValueChange = { search = it},
                textStyle = TextStyle(
                    color = BlackColor,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(fonts = MontserratMedium),
                    textAlign = TextAlign.Start
                ),
                leadingIcon={
                    FaIcon(faIcon = FaIcons.Search, size = 20.dp, tint = GREYCOLOR)
                },
                placeholder={
                    Text(
                        text = stringResource(R.string.search),
                        style = TextStyle(
                            color = Color(0xFF565a5e),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Start
                        ),
                        fontFamily = FontFamily(fonts = MontserratRegular),
                        modifier = Modifier.padding(start = 0.dp)
                    )
                },
            )
        }

        ExposedDropdownMenu(expanded = exp, onDismissRequest = {
            exp = false
        },modifier = Modifier.width(with(LocalDensity.current) { textfieldSize.width.toDp() })) {
            if(items.isNotEmpty()){
                items.forEachIndexed { i, v ->
                    DropdownMenuItem(
                        onClick = {
                            //search = v
                            exp = false
                            selectedItemData(i,v)
                        }
                    ) {
                        ListItem(
                            text = { Text(Helpers.removeNull(v), style = TextStyle(
                                color = TextColor,
                                fontSize = 13.sp,
                                fontFamily = FontFamily(fonts = RobotoRegular)
                            )
                            )},
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
                }
            }else if(loading){
                Box(contentAlignment = Alignment.Center, modifier = Modifier.height(56.dp)){
                    CircularProgressIndicator()
                }
            }
        }
    }
}
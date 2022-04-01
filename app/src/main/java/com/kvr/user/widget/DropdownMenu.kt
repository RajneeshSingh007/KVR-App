package com.kvr.user.widget

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.kvr.user.ui.theme.BlackColor
import com.kvr.user.ui.theme.MontserratMedium
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DropdownMenu(items: List<String> = ArrayList<String>(0), preSelected:String = "", placeholder:String ="", selectedItemData:(pos:Int, value:String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var textfieldSize by remember { mutableStateOf(Size.Zero) }
    var selectedText by remember { mutableStateOf("") }

    LaunchedEffect(key1 = "DropdownMenu"){
        if(preSelected.isNotEmpty() && selectedText.isEmpty()){
            selectedText = preSelected
            selectedItemData(items.indexOf(preSelected),preSelected)
        }
    }

    val labelColor =
        if (expanded) MaterialTheme.colors.primary.copy(alpha = ContentAlpha.high)
        else MaterialTheme.colors.onSurface.copy(ContentAlpha.medium)
    val trailingIconColor = MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.IconOpacity)

    val rotation: Float by animateFloatAsState(if (expanded) 180f else 0f)

    val focusManager = LocalFocusManager.current

    val icon = if(expanded){
        Icons.Filled.KeyboardArrowUp
    }else {
        Icons.Filled.KeyboardArrowDown
    }
    Column(modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .clip(shape = RoundedCornerShape(8.dp))
        .background(color = Color(0xFFf3f3f3))) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFf3f3f3),
                    shape = MaterialTheme.shapes.small.copy(
                        bottomEnd = ZeroCornerSize,
                        bottomStart = ZeroCornerSize
                    )
                )
                .onGloballyPositioned { textfieldSize = it.size.toSize() }
                .clip(
                    MaterialTheme.shapes.small.copy(
                        bottomEnd = ZeroCornerSize,
                        bottomStart = ZeroCornerSize
                    )
                )
                .clickable {
                    expanded = !expanded
                    focusManager.clearFocus()
                }
                .padding(start = 16.dp, end = 12.dp, top = 7.dp, bottom = 10.dp)
        ) {
            Column(Modifier.padding(end = 32.dp)) {
                ProvideTextStyle(value = MaterialTheme.typography.caption.copy(color = labelColor)) {
                    Text(text = placeholder)
                }
                Text(
                    text = selectedText,
                    modifier = Modifier.padding(top = 1.dp),
                    style = TextStyle(
                        color = BlackColor,
                        fontSize = 15.sp,
                        fontFamily = FontFamily(fonts = MontserratMedium),
                        textAlign = TextAlign.Start
                    )
                )
            }
            Icon(
                imageVector = icon,
                contentDescription = "Change",
                tint = trailingIconColor,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(top = 4.dp)
                    .rotate(rotation)
            )

        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { textfieldSize.width.toDp() })
        ) {
            items.forEachIndexed { i, v ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        selectedText = v
                        selectedItemData(i,v)
                    }
                ) {
                    Text(v, modifier = Modifier.padding(top = 10.dp))
                }
            }
        }
    }

}
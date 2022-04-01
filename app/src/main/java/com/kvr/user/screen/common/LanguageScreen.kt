package com.kvr.user.screen.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.kvr.user.Navigation
import com.kvr.user.ui.theme.GREYCOLOR
import kotlinx.coroutines.launch
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kvr.user.R
import com.kvr.user.ui.theme.HeadingColor
import com.kvr.user.ui.theme.MontserratBold

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LanguageScreen(showDialog: Boolean, onDismiss: (isChanged:Boolean, lang:String)->Unit) {
    val languageList = mutableListOf<String>("English", "తెలుగు")
    if(showDialog){
        Dialog(
            onDismissRequest = { onDismiss(false,"") },
            DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        ) {
            Box(
                contentAlignment= Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.change_language),
                        style = TextStyle(
                            color = HeadingColor,
                            fontSize = 16.sp,
                        ),
                        fontFamily = FontFamily(fonts = MontserratBold),
                        modifier = Modifier
                            .padding(start = 16.dp)
                    )
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                    LazyColumn {
                        items(languageList) {
                            ListItem(
                                text = { Text(it)},
                                icon = {
                                    FaIcon(faIcon = FaIcons.Language, size = 20.dp, tint = GREYCOLOR)
                                },
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

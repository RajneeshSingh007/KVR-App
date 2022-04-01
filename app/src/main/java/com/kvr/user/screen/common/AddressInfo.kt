package com.kvr.user.screen.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.kvr.user.ui.theme.*


@Composable
fun AddressInfo( title:String = "", name:String="",address:String="", showEditBtn:Boolean = true, btnText:String="Edit",btnModifier:Modifier = Modifier.fillMaxWidth(), editClick: ()-> Unit = {},showBtn:Boolean = false, deleteBtn:()-> Unit = {},editBtn:()-> Unit = {},selected:()->Unit={}){
    Row(modifier = Modifier
        .fillMaxWidth()
        .then(btnModifier), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
        if(title.isNotEmpty()){
            Text(
                text = title,
                style = TextStyle(
                    color = HeadingColor,
                    fontSize = 16.sp,
                ),
                fontFamily = FontFamily(fonts = MontserratBold),
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(0.65f)
            )
        }
        if(showEditBtn){
            Button(
                onClick = {
                    editClick()
                }, modifier = Modifier
                    .fillMaxWidth(0.2f)
                    .weight(0.25f)
                    .height(36.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFe1e1e1),
                    contentColor = Color(0xFFe1e1e1)
                ),
                shape = RoundedCornerShape(2.dp)

            ) {
                Text(
                    text =btnText,
                    style = TextStyle(
                        color = PrimaryColor,
                        fontSize = 15.sp
                    ),
                    fontFamily = FontFamily(fonts = RobotoRegular),
                )
            }
        }
        if(title.isNotEmpty() || showEditBtn){
            Spacer(modifier = Modifier.weight(0.05f))
        }
    }
    if(address.isNotEmpty() && name.isNotEmpty()){
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        Surface( elevation = 6.dp,
            color = MaterialTheme.colors.surface,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selected()
                    }, shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .fillMaxHeight()) {
                    if(showBtn){
                        Row(horizontalArrangement = Arrangement.SpaceBetween,verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()){
                            Text(
                                text = name,
                                style = TextStyle(
                                    color = BlackColor,
                                    fontSize = 15.sp,
                                    textAlign = TextAlign.Start
                                ),
                                fontFamily = FontFamily(fonts = MontserratMedium),
                                modifier = Modifier.weight(0.75f)
                            )
                            Row(horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically,modifier = Modifier.weight(0.25f)){
                                IconButton(onClick = {editBtn() }) {
                                    FaIcon(faIcon = FaIcons.Edit, size = 20.dp, tint = GREYCOLOR)
                                }
                                IconButton(onClick = {deleteBtn() }) {
                                    FaIcon(faIcon = FaIcons.Trash, size = 20.dp, tint = GREYCOLOR)
                                }
                            }
                        }
                    }else{
                        Text(
                            text = name,
                            style = TextStyle(
                                color = BlackColor,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Start
                            ),
                            fontFamily = FontFamily(fonts = MontserratMedium),
                        )
                    }
                    Spacer(modifier = Modifier.padding(vertical = 2.dp))
                    Text(
                        text = address,
                        style = TextStyle(
                            color = GREYCOLOR,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Start
                        ),
                        fontFamily = FontFamily(fonts = MontserratBold),
                    )

                }
            }
        }
    }
}
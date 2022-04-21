package com.kvr.user.screen.common

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.kvr.user.MainActivity
import com.kvr.user.R
import com.kvr.user.ui.theme.MontserratMedium
import com.kvr.user.ui.theme.WhiteColor


@Composable
fun SupportButton(widthFraction:Float = 0.8f, modifier: Modifier = Modifier.height(40.dp)) {
    val context = LocalContext.current as Activity
    Button(
        onClick = {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:+911234567890")
            context.startActivity(intent)
        }, modifier = Modifier
            .fillMaxWidth(widthFraction)
            .clip(shape = RoundedCornerShape(8.dp))
            .then(modifier),
        contentPadding = PaddingValues(8.dp)
    ) {
        Text(
            text = stringResource(R.string.support),
            style = TextStyle(
                color = WhiteColor,
                fontSize = 15.sp
            ),
            fontFamily = FontFamily(fonts = MontserratMedium),
        )
    }
}
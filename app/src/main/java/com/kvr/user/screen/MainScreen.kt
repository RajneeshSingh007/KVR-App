package com.kvr.user.screen

import android.app.Activity
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.interceptors.loggingResponseInterceptor
import com.kvr.user.BaseApplication
import com.kvr.user.MainActivity
import com.kvr.user.Navigation
import com.kvr.user.Screen
import com.kvr.user.screen.common.LanguageScreen
import com.kvr.user.screen.common.Sidebar
import com.kvr.user.utils.Constants
import com.kvr.user.utils.Helpers
import com.kvr.user.widget.ShowProgressDialog
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
//@Preview(showSystemUi = true)
@Composable
fun MainScreen() {
    val pref = BaseApplication.appContext.appPref
    val context = LocalContext.current as Activity
    val navHostController = rememberNavController()
    val scrollState = rememberScrollState()
    val isLoggedIn = remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val languageDialog = remember{mutableStateOf(false)}
    val showLoader = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val openDrawer = {
        scope.launch {
            drawerState.open()
        }
    }
    val closeDrawer = {
        scope.launch {
            drawerState.close()
        }
    }
    val scrollReset = {
        scope.launch {
            scrollState.scrollTo(0)
        }
    }
    //check user loggedIn or not
    val navigate = {
        scope.launch {
            val value = pref.getString(Constants.IS_LOGGEDIN)
            val token = pref.getString(Constants.ACCESS_TOKEN)
            var isLoggedIn = false
            if (value.isNullOrEmpty()){

            }else{
                FuelManager.instance.baseHeaders = mapOf("Authorization" to "Bearer ${token}")
                isLoggedIn = value.toBoolean()
            }
            showLoader.value = false
            navHostController.navigate(if(isLoggedIn) Screen.HomeScreen.route else Screen.SignIn.route){
                popUpTo(Screen.SplashScreen.route){
                    inclusive = true
                }
            }
        }
    }

    //check user logout
    val logoutUser = {
        showLoader.value = false
        isLoggedIn.value = false
        scope.launch {
            pref.putString(Constants.IS_LOGGEDIN, "false")
            pref.putString(Constants.ACCESS_TOKEN, "")
            pref.putString(Constants.PREF_ADD, "")
            navHostController.navigate(Screen.SignIn.route){
                popUpTo(Screen.HomeScreen.route){
                    inclusive = true
                }
            }
        }
    }

    //change lang
     fun changeLang(hide:Boolean,lang:String){
        languageDialog.value = false
        if(hide){
            closeDrawer()
            Helpers.setLocale(context,if(lang=="తెలుగు") "te" else "en")
        }
    }
    Scaffold{
        ModalDrawer(
            drawerState = drawerState,
            gesturesEnabled = drawerState.isOpen,
            drawerContent = {
                Sidebar(navHostController, drawerClick = {
                    closeDrawer()
                }, logoutClick = {
                    closeDrawer()
                    logoutUser()
                }, langClick = {
                    languageDialog.value = true
                    closeDrawer()
                })
            },
        ){
            Surface(color = Color.White, modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .verticalScroll(state = scrollState)
            ) {
                LanguageScreen(showDialog = languageDialog.value, onDismiss = {
                  hide, lang ->  changeLang(hide, lang)
                })
                ShowProgressDialog(showDialog = showLoader.value) {
                    showLoader.value = false
                }
                Navigation(navHostController = navHostController, drawerClick = {
                    openDrawer()
                }, resetScroll = {
                    scrollReset()
                }, navigate = {
                    navigate()
                }, loader = {
                    showLoader.value = it
                }){
                    closeDrawer()
                    logoutUser()
                }
            }
        }
    }
}
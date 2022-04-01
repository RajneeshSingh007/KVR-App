package com.kvr.user

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kvr.user.model.Brandsdata
import com.kvr.user.screen.*
import com.kvr.user.utils.Helpers
import com.kvr.user.utils.NavParcel

@Composable
fun Navigation(navHostController: NavHostController, drawerClick: ()-> Unit = {}, resetScroll: ()-> Unit = {},navigate: ()-> Unit = {},loader: (show:Boolean)-> Unit = {},logout: ()-> Unit = {}) {
    NavHost(navController = navHostController, startDestination = Screen.SplashScreen.route) {
        composable(route = Screen.SplashScreen.route){
            resetScroll()
            SplashScreen(navController = navHostController){
                navigate()
            }
        }
        composable(route = Screen.SignIn.route){
            resetScroll()
            SignIn(navController = navHostController){
                loader(it)
            }
        }
        composable(route = Screen.Signup.route){
            resetScroll()
            Signup(navController= navHostController){
                loader(it)
            }
        }
        composable(route = Screen.ForgotPass.route){
            resetScroll()
            ForgotPass(navController= navHostController)
        }
        composable(route = Screen.ForgotSuccess.route){
            resetScroll()
            ForgotSuccess(navController= navHostController)
        }
        composable(route = Screen.HomeScreen.route){
            resetScroll()
            HomeScreen(navController = navHostController){
                drawerClick()
            }
        }
        composable(route = Screen.ProductList.route+"/{name}/{id}",
            arguments = listOf(
                navArgument("name") {
                    defaultValue = ""
                },
                navArgument("id") {
                    defaultValue = -1
                },
        )
        ){
            resetScroll()
            val id = it.arguments!!.getInt("id", -1)
            val name = Helpers.removeNull(it.arguments!!.getString("name", ""))
            ProductList(navController = navHostController, name = name, id = id){
                drawerClick()
            }
        }
        composable(route = Screen.ProductDetails.route+"/{name}/{id}",
            arguments = listOf(
                navArgument("name") {
                    defaultValue = ""
                },
                navArgument("id") {
                    defaultValue = -1
                },
            )
        ){
            resetScroll()
            val id = it.arguments!!.getInt("id", -1)
            val name = Helpers.removeNull(it.arguments!!.getString("name", ""))
            ProductDetails(navController = navHostController, name = name, id = id){
                loader(it)
            }
        }
        composable(route = Screen.OrderScreen.route+"/{screenType}",
            arguments = listOf(navArgument("screenType"){ defaultValue = -1},
            )){
            resetScroll()
            OrderScreen(navController = navHostController, screenType = it.arguments?.getInt("screenType",-1)){
                loader(it)
            }
        }
        composable(route = Screen.TrackOrder.route+"/{id}",arguments = listOf(
            navArgument("id") {
                defaultValue = -1
            }
        )){
            resetScroll()
            val id = it.arguments!!.getInt("id", -1)
            TrackOrder(navController= navHostController,id)
        }
        composable(route = Screen.PaymentScreen.route+"/{total}",arguments = listOf(
            navArgument("total") {
                defaultValue = ""
            }
        )){
            resetScroll()
            val total = Helpers.removeNull(it.arguments!!.getString("total", ""))
            PaymentScreen(navController= navHostController,total){
                loader(it)
            }
        }
        composable(route = Screen.PaymentSuccess.route+"/{total}",arguments = listOf(
            navArgument("total") {
                defaultValue = ""
            }
        )){
            resetScroll()
            val total = Helpers.removeNull(it.arguments!!.getString("total", ""))
            PaymentSuccess(navController= navHostController,total)
        }
        composable(route = Screen.ProfileScreen.route){
            resetScroll()
            ProfileScreen(navController= navHostController){
                loader(it)
            }
        }
        composable(route = Screen.ResetPasswordScreen.route){
            resetScroll()
            ResetPassScreen(navController= navHostController, loader = {
                loader(it)
            }){
                logout()
            }
        }
        composable(route = Screen.MyAddress.route){
            resetScroll()
            MyAddress(navController= navHostController){
                loader(it)
            }
        }
        composable(route = Screen.AddressScreen.route+"/{name}/{address}/{id}",arguments = listOf(
            navArgument("name") {
                defaultValue = ""
            },
            navArgument("address") {
                defaultValue = ""
            },
            navArgument("id") {
                defaultValue = ""
            },
        )){
            resetScroll()
            val name = Helpers.removeNull(it.arguments!!.getString("name", ""))
            val address = Helpers.removeNull(it.arguments!!.getString("address", ""))
            val id = Helpers.removeNull(it.arguments!!.getString("id", ""))
            AddressScreen(navController= navHostController,name,address,id){
                loader(it)
            }
        }
        composable(route = Screen.AddressScreen.route){
            resetScroll()
            AddressScreen(navController= navHostController,"","",""){
                loader(it)
            }
        }
        composable(route = Screen.SearchByVehicle.route){
            resetScroll()
            SearchByVehicle(navController= navHostController)
        }
        composable(route = Screen.NotificationScreen.route){
            resetScroll()
            NotificationScreen(navController= navHostController)
        }
        composable(route = Screen.CouponScreen.route){
            resetScroll()
            CouponScreen(navController= navHostController)
        }
    }
}
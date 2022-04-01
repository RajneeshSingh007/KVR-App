package com.kvr.user

sealed class Screen(val route: String) {
    object SignIn: Screen(route = "sign_screen")
    object SplashScreen: Screen(route = "splash_screen")
    object HomeScreen: Screen(route = "home_screen")
    object ProductList: Screen(route = "products_screen")
    object ProductDetails: Screen(route = "productdetails_screen")
    object OrderScreen: Screen(route = "order_screen")
    object PaymentScreen: Screen(route = "payment_screen")
    object PaymentSuccess: Screen(route = "paymentsuccess_screen")
    object TrackOrder: Screen(route = "trackorder_screen")
    object ProfileScreen: Screen(route = "profile_screen")
    object ResetPasswordScreen: Screen(route = "resetpass_screen")
    object Signup: Screen(route = "signup_screen")
    object ForgotPass: Screen(route = "forgotPass_screen")
    object MyAddress: Screen(route = "myaddress_screen")
    object ForgotSuccess: Screen(route = "forgotsuccess_screen")
    object AddressScreen: Screen(route = "address_screen")
    object SearchByVehicle: Screen(route = "searchbyvehicle_screen")
    object NotificationScreen: Screen(route = "notification_screen")
    object CouponScreen: Screen(route = "coupon_screen")

}

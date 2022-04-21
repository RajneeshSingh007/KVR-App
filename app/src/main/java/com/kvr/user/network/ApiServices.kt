package com.kvr.user.network

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FileDataPart
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.core.extensions.cUrlString
import com.github.kittinunf.result.failure
import com.kvr.user.BaseApplication
import com.kvr.user.model.*
import com.kvr.user.utils.Helpers
import java.io.File

object ApiServices {

    suspend fun loginApi(loginReq: LoginReq) : Response<Login> {
        val body = listOf("username" to loginReq.username.toString(), "password" to loginReq.password.toString(),"device_token" to loginReq.device_token.toString())
        return Network<Login>(Login::class.java,"/login", body).networkCall(false,0)
    }

    suspend fun resendOtpApi(otpReq: OtpReq) : Response<Otp> {
        val body = listOf("phone" to otpReq.phone)
        return Network<Otp>(Otp::class.java,"/resend-otp", body).networkCall(false,0)
    }

    suspend fun registerApi(registerReq: RegisterReq) : Response<Otp> {
        val body = listOf(
            "first_name" to registerReq.first_name.toString(),
            "email" to registerReq.email.toString(),
            "password" to registerReq.password.toString(),
            "c_password" to registerReq.c_password.toString(),
            "last_name" to registerReq.last_name.toString(),
            "phone" to registerReq.phone.toString(),
            "role_id" to registerReq.role_id.toString())
        return Network<Otp>(Otp::class.java,"/signup", body).networkCall(false,0)
    }

    suspend fun verifyOtp(otpReq: OtpReq) : Response<Register> {
        val body = listOf("otp" to otpReq.otp.toString())
        return Network<Register>(Register::class.java,"/verify-otp", body).networkCall(false,0)
    }

    suspend fun getBrandsApi() : Response<Brands> {
        return Network<Brands>(Brands::class.java,"/all-brands", null).networkCall(methodType=1)
    }

    suspend fun getBannersApi() : Response<Banner> {
        return Network<Banner>(Banner::class.java,"/banners", null).networkCall(methodType=1)
    }

    suspend fun getBrandsProductApi(brandsId:Int= 0) : Response<Products> {
        return Network<Products>(Products::class.java,"/products-by-brand/${brandsId}", null).networkCall(methodType=1)
    }

    suspend fun getHomeProductApi(type:String = "") : Response<ProductList> {
        return Network<ProductList>(ProductList::class.java,"/products-variation/?type=${type}", null).networkCall(methodType=1)
    }

    suspend fun getProductDetailsApi(productId:Int= 0) : Response<ProductDetails> {
        return Network<ProductDetails>(ProductDetails::class.java,"/product/${productId}", null).networkCall(methodType=1)
    }

    suspend fun getCategoryListApi() : Response<CategoryList> {
        return Network<CategoryList>(CategoryList::class.java,"/all-categories/", null).networkCall(methodType=1)
    }

    suspend fun getSearchProductsApi(query:String="") : Response<ProductList> {
        return Network<ProductList>(ProductList::class.java,"/products/?query=${query}", null).networkCall(methodType=1)
    }

    suspend fun addToCartApi(cartReq: CartReq, isUpdate: Boolean) : Response<CommonResponse> {
        val body = listOf("product_id" to cartReq.product_id.toString(),
            "discount" to cartReq.discount.toString(),
            "amount" to cartReq.amount.toString(),
            "total" to cartReq.total.toString(),
            "qty" to cartReq.qty.toString())
        return Network<CommonResponse>(CommonResponse::class.java, if(isUpdate) "/update-cart/${cartReq.cartId}" else "/add-to-cart", body).networkCall(methodType=0)
    }

    suspend fun changePassApi(changePass: ChangePass) : Response<CommonResponse> {
        val body = listOf("password" to changePass.password.toString(),
            "c_password" to changePass.c_password.toString())
        return Network<CommonResponse>(CommonResponse::class.java, "/change-password", body).networkCall(methodType=0)
    }

    suspend fun getCartsListApi() : Response<Carts> {
        return Network<Carts>(Carts::class.java,"/my-cart/", null).networkCall(methodType=1)
    }

    suspend fun removeFromCartApi(cartId:Int) : Response<CommonResponse> {
        return Network<CommonResponse>(CommonResponse::class.java,"/my-cart/${cartId}", null).networkCall(methodType=2)
    }

    suspend fun getAddressListApi() : Response<AddressList> {
        return Network<AddressList>(AddressList::class.java,"/addresses", null).networkCall(methodType=1)
    }

    suspend fun deleteAddApi(addressId:Int) : Response<CommonResponse> {
        return Network<CommonResponse>(CommonResponse::class.java,"/address/${addressId}", null).networkCall(methodType=2)
    }

    suspend fun addAddApi(addressesReq: AddressesReq, isUpdate: Boolean) : Response<CommonResponse> {
        if(isUpdate){
            val body = listOf(
                "first_name" to addressesReq.first_name,
                "last_name" to addressesReq.last_name,
                "mobile_no" to addressesReq.mobile_no,
                "address1" to addressesReq.address1,
                "zipcode" to addressesReq.zipcode,
                "city" to addressesReq.city,
                "address2" to addressesReq.address2,
                "address_id" to addressesReq.address_id,
                )
            return Network<CommonResponse>(CommonResponse::class.java, "/update-address", body).networkCall(methodType=0)
        }else{
            val body = listOf(
                "first_name" to addressesReq.first_name,
                "last_name" to addressesReq.last_name,
                "mobile_no" to addressesReq.mobile_no,
                "address1" to addressesReq.address1,
                "zipcode" to addressesReq.zipcode,
                "city" to addressesReq.city,
                "address2" to addressesReq.address2,
            )
            return Network<CommonResponse>(CommonResponse::class.java, "/address", body).networkCall(methodType=0)
        }
    }

    suspend fun placeOrderApi(placeOrderReq: PlaceOrderReq) : Response<CommonResponse> {
        val body = listOf(
            "payment_method" to placeOrderReq.payment_method,
            "billing_address_id" to placeOrderReq.billing_address_id,
            "shipping_address_id" to placeOrderReq.shipping_address_id,
            "coupon_code" to placeOrderReq.couponCode
        )
        return Network<CommonResponse>(CommonResponse::class.java, "/place-the-order", body).networkCall(methodType=0)
    }

    suspend fun getProfileApi() : Response<Profile> {
        return Network<Profile>(Profile::class.java,"/my-profile/", null).networkCall(methodType=1)
    }

    suspend fun updateProfileApi(profileReq: ProfileReq, file:File?) : Response<CommonResponse> {
        val body = listOf(
            "first_name" to profileReq.first_name,
            "last_name" to profileReq.last_name,
            "phone" to profileReq.phone,
            "email" to profileReq.email
        )
        return Network<CommonResponse>(CommonResponse::class.java,"/update-my-profile", body).networkCall(methodType=3, file = file)
    }


    suspend fun getOrdersListApi() : Response<OrderList> {
        return Network<OrderList>(OrderList::class.java,"/my-orders/", null).networkCall(methodType=1)
    }

    suspend fun getOrdersDetailsApi(orderId:Int = -1) : Response<Orders> {
        return Network<Orders>(Orders::class.java,"/order-info/${orderId}/", null).networkCall(methodType=1)
    }

    suspend fun getNotificationApi() : Response<Notification> {
        return Network<Notification>(Notification::class.java,"/notifications/", null).networkCall(methodType=1)
    }

    suspend fun getCouponsApi() : Response<Coupons> {
        return Network<Coupons>(Coupons::class.java,"/coupons/", null).networkCall(methodType=1)
    }

    suspend fun forgotPasswordApi(forgotPass: ForgotPass) : Response<CommonResponse> {
        val body = listOf("email" to forgotPass.email)
        return Network<CommonResponse>(CommonResponse::class.java, "/forgot-password", body).networkCall(methodType=0)
    }

    suspend fun resetPasswordApi(resetForgotPass: ResetForgotPass) : Response<CommonResponse> {
        val body = listOf("otp" to resetForgotPass.otp,"password" to resetForgotPass.password)
        return Network<CommonResponse>(CommonResponse::class.java, "/reset-password", body).networkCall(methodType=0)
    }

    suspend fun searchProductsApi(searchProducts: SearchProducts) : Response<ProductList> {
        val body = listOf("variation[Size]" to searchProducts.variation,
            "part_code" to searchProducts.partCode,
            "variation[Engine]" to searchProducts.engine,
           // "part_no" to searchProducts.partNumber,
            "name" to searchProducts.name)
        //Log.e("body", "${body.toString()}")
        return Network<ProductList>(ProductList::class.java,"/products", body).networkCall(methodType=0)
    }

}
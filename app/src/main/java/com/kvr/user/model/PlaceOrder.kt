package com.kvr.user.model

data class PlaceOrderReq(val payment_method: String,val coupon_applied: String="0",val couponCode: String = "", val shipping_address_id:String, val billing_address_id:String)
package com.kvr.user.model

import com.razorpay.PaymentData

interface PaymentListerner {

    fun success(paymentID:String?,paymentData: PaymentData?)

    fun failure(errorCode:Int,paymentID:String?,paymentData: PaymentData?)

}
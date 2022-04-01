package com.kvr.user.model

data class Otp (     val data : Any,
                       val status : Boolean,
                       val message : String)

data class OtpReq ( val phone : String, val otp:String)

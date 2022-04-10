package com.kvr.user.model

data class ForgotPass(val email: String)

data class ResetForgotPass(val password: String,val otp: String)
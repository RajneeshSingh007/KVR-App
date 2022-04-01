package com.kvr.user.model


data class LoginReq(var username: String = "", var password: String = "",var device_token: String = "")

data class Login (     val data : LoginData,
                       val status : Boolean,
                       val message : String)

data class LoginData (
    val ship_city : String,
    val bill_city : String,
    val bill_zip : String,
    val bill_company : String,
    val ship_company : String,
    val bill_address1 : String,
    val last_name : String,
    val photo : String,
    val created_at : String,
    val email_token : String,
    val bill_address2 : String,
    val ship_zip : String,
    val bill_country : String,
    val token : String,
    val updated_at : String,
    val role_id : Int,
    val phone : String,
    val ship_address1 : String,
    val ship_address2 : String,
    val id : Int,
    val state_id : String,
    val first_name : String,
    val email : String,
    val ship_country : String
)
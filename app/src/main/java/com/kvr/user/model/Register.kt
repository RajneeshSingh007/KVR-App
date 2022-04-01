package com.kvr.user.model

data class RegisterReq(var role_id:Int = 1, var phone: String = "",  var first_name: String = "", var email: String = "",var password: String = "",var c_password: String = "",var last_name: String = "")

data class Register (
    val data : RegisterData,
    val status : Boolean,
    val message : String
)

data class RegisterData (

    val updated_at : String,
    val last_name : String,
    val created_at : String,
    val id : Int,
    val first_name : String,
    val email : String,
    val token : String

)

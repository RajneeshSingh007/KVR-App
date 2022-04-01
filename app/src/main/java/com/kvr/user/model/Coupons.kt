package com.kvr.user.model

data class CouponsData (

    val user_role_id : Int = 0,
    val updated_at : String = "",
    val expiry_date : String = "",
    val discount : Int = 0,
    val created_at : String="",
    val id : Int? = 0,
    val title : String="",
    val no_of_times : Int=0,
    val type : String="",
    val code_name : String="",
    val status : Int=0

)


data class Coupons (

    val data: List<CouponsData>,
    val message : String,
    val status : Boolean

)

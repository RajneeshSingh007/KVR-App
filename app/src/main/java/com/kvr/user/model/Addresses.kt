package com.kvr.user.model

data class AddressesReq(val first_name:String,
                        val last_name:String,
                        val mobile_no:String,
                        val address1:String,
val zipcode:String,
val city:String,
val address2:String, val address_id:String)

data class AddressList (
    val data: List<AddressData>,
    val message : String,
    val status : Boolean
)

data class AddressData (
    val country : String,
    val address2 : String,
    val city : String,
    val address1 : String,
    val mobile_no : String,
    val last_name : String,
    val created_at : String,
    val zipcode : String,
    val updated_at : String,
    val user_id : Int,
    val company : String,
    val id : Int,
    val first_name : String
)

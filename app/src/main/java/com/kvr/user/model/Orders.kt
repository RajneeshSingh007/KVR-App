package com.kvr.user.model

data class OrderList (
    val data: List<OrderData>,
    val message : String,
    val status : Boolean

)

data class Orders (
    val data : OrderData,
    val success : Boolean,
    val message : String,
    val status : Boolean
)


data class OrderData (
    val shipping_info : String,
    val reason : String,
    val discount : String,
    val created_at : String,
    val cart : List<CartData>,
    val tranaction : Tranaction,
    val state_price : Int,
    val order_status : String,
    val billing_info : String,
    val shipping : String,
    val updated_at : String,
    val charge_id : String,
    val id : Int,
    val state : String,
    val payment_method : String,
    val txnid : String,
    val billing_address_id : Int,
    val shipping_address_id : Int,
    val payment_status : String,
    val currency_value : Int,
    val tax : Double,
    val user_id : Int,
    val currency_sign : String,
    val transaction_number : String,
    val tracks_data: List<Tracks_data>,
    val status : Int,
)

data class Tranaction (
    val txn_id : String,
    val amount : Double,
    val user_email : String,
    val updated_at : String,
    val currency_sign : String,
    val currency_value : Int,
    val created_at : String,
    val id : Int,
    val order_id : Int
)

data class Tracks_data (
    val updated_at : String,
    val created_at : String,
    val id : Int,
    val title : String,
    val order_id : Int
)




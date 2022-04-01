package com.kvr.user.model


data class Notification (
    val data: List<NotifyData>,
    val message : String,
    val status : Boolean
)

data class NotifyData (
    val is_read : Int,
    val updated_at : String,
    val user_id : Int,
    val created_at : String,
    val id : Int,
    val title : String,
    val message : String,
    val order_id : Int
)


package com.kvr.user.model

data class Brands (
    val data: List<Brandsdata>,
    val status : Boolean,
    val message : String
)

data class Brandsdata (
    val updated_at : String,
    val is_popular : Int,
    val name : String="",
    val photo : String="",
    val created_at : String,
    val id : Int
)

package com.kvr.user.model

data class CategoryList(
    val data: List<Category>,
    val status : Boolean,
    val message : String
)

data class Category (

    val updated_at : String,
    val serial : Int,
    val name : String,
    val photo : String,
    val created_at : String,
    val id : Int,
    val meta_keywords : String,
    val slug : String,
    val meta_descriptions : String,
    val is_feature : Int,
    val status : Int

)

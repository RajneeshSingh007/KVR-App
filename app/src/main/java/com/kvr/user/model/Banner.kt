package com.kvr.user.model

data class Banner (
    val data: List<BannerData>,
    val message : String,
    val status : Boolean
)

data class BannerData (
    val photo : String,
)

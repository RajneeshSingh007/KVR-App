package com.kvr.user.model

data class CartReq(
    var cartId:String="",
    var product_id:String,
    var amount:String,
    var discount:String,
    var total:String,
    var qty:String="1") {
}

data class Carts (
    val data: List<CartData>,
    val success : Boolean = false,
    val status : Boolean = false,
    val message : String
)

data class CartData (

    val amount : Double,
    var total : Double,
    val item : ProductsData? = null,
    val updated_at : String,
    val user_id : Int,
    val product_id : Int,
    var qty : Int,
    val discount : Double,
    val created_at : String,
    val id : Int,
    val brand:Brandsdata,
    val supplier:Supplier,
    val product_images: List<ProductImages>,
    val product_name:String,
)

data class ProductImages (

    val updated_at : String,
    val item_id : Int,
    val photo : String="",
    val created_at : String,
    val id : Int

)


data class Supplier (
    val updated_at : String,
    val city : String,
    val name : String="",
    val created_at : String,
    val id : Int,
    val info : String
)


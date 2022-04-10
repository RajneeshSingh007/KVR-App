package com.kvr.user.model

data class Products (
    val data : PData,
    val message : String,
    val status : Boolean,
)

data class ProductList (
    val data : List<ProductsData>,
    val message : String,
    val status : Boolean,
    var type:Int = 0
)

data class ProductDetails (
    val data : ProductsData? = null,
    val message : String = "",
    val status : Boolean = false
)

data class PData (

    val updated_at : String="",
    val is_popular : Int=0,
    val name : String="",
    val photo : String="",
    val created_at : String="",
    val id : Int=0,
    val slug : String="",
    val status : Int=0,
    val products: List<ProductsData>
)

data class ProductsData (
    val date : String,
    val discount_price : Double,
    val item_type : String,
    val link : String,
    val created_at : String,
    val remark : String,
    val video : String,
    val meta_keywords : String,
    val subcategory_id : Int,
    val license_name : String,
    val file : String,
    val category_id : Int,
    val updated_at : String,
    val file_type : String,
    val details : String="",
    val id : Int,
    val sku : String,
    val stock : Int,
    val part_code : String,
    val slug : String,
    val affiliate_link : String,
    val thumbnail : String,
    val specification_name : String,
    val photo : String = "",
    val tax_id : Int,
    val license_key : String,
    val brand_id : Int,
    val tags : String,
    val part_no : Double = 0.0,
    val meta_description : String,
    val is_specification : Int,
    val is_type : String,
    val childcategory_id : Int,
    val sort_details : String,
    val name : String = "",
    val specification_description : String,
    val previous_price : Double=0.0,
    val status : Int,
    val brand : Brandsdata,
    val tax : Tax,
    val galleries: List<Galleries>,
    val category : Category,
    val attributes: List<Attributes>,
)

data class Attributes (

    val updated_at : String,
    val item_id : Int,
    val name : String="",
    val created_at : String,
    val id : Int,
    val keyword : String

)


data class Tax (
    val updated_at : String,
    val name : String="",
    val created_at : String,
    val id : Int,
    val value : Int,
    val status : Int
)

data class Galleries (
    val updated_at : String,
    val item_id : Int,
    val photo : String="",
    val created_at : String,
    val id : Int
)

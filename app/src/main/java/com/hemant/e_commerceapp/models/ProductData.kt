package com.hemant.e_commerceapp.models

data class ProductData(
    val id : Int,
    val title : String,
    val price : Number,
    val description : String,
    val images : List<String>,
    val category : CategoryData
)

data class CategoryData(
    val id : Int,
    val name : String,
    val image : String
)

data class LoginCrediential(
    val email : String,
    val password : String
)

data class LoginResponse(
    val access_token : String,
    val refresh_token : String
)

data class ProfileResponse(
    val id : Int,
    val email : String,
    val password : String,
    val name : String,
    val role : String,
    val avatar : String
)
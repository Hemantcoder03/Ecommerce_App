package com.hemant.e_commerceapp.api_service

import android.icu.text.CaseMap.Title
import com.hemant.e_commerceapp.models.LoginCrediential
import com.hemant.e_commerceapp.models.LoginResponse
import com.hemant.e_commerceapp.models.ProductData
import com.hemant.e_commerceapp.models.ProfileResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

const val BASE_URL = "https://api.escuelajs.co/api/v1/"

interface ApiInterface {

    //get the all products list
    @GET("products")
    fun getProducts(): Call<ArrayList<ProductData>>

    //get the single product
    @GET("products")
    fun getProduct(@Query("id") id : Int): Call<ProductData>

    //get the searched products list
    @GET("products")
    fun getSearchedProducts(@Query("title") title: String): Call<ArrayList<ProductData>>

    //get the products by category filter
    @GET("products")
    fun getCategoryProducts(@Query("categoryId") categoryId : Int): Call<ArrayList<ProductData>>

    //get the products by category filter
    @GET("products")
    fun getPriceRangeProducts(@Query("price_min") price_min : Int, @Query("price_max") price_max : Int): Call<ArrayList<ProductData>>

    //get the products by category filter
    @GET("products")
    fun getCombFilterProducts(@Query("price_min") price_min : Int, @Query("price_max") price_max : Int,@Query("categoryId") categoryId : Int): Call<ArrayList<ProductData>>

    //login get response form of token
    @POST("auth/login")
    fun doAuthentication(@Body loginCrediential: LoginCrediential): Call<LoginResponse>

    //get the user data using token
    @GET("auth/profile")
    fun doAuthorization(@Header("Authorization") authorization : String): Call<ProfileResponse>
}

object ApiService {

    val apiInterface: ApiInterface by lazy {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiInterface::class.java)
    }
}
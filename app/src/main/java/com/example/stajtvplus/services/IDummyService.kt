package com.example.stajtvplus.services


import com.example.stajtvplus.models.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface IDummyService {

    @POST("/auth/login")
    fun login(@Body jwtUser: JWTUser) : Call<JWTData>

    @GET("products")
    fun products() : Call<DummyProduct>

    @GET("products/search")
    fun filterProducts(@Query("q") filterText : String) : Call<DummyProduct>

    // kategöri
    @GET("products/categories")
    fun getCategories(): Call<List<String>>

    @GET("products/category/{category}")
    fun getProductsByCategory(@Path("category") category: String): Call<Category>

    @GET("users/{userId}")
    fun getUserInfoById(@Path("userId") userId: String): Call<JWTData>

    @PUT("user/{id}")
    fun updateUserInfo(@Path("id") userId: String, @Body updatedUserInfo: JWTData): Call<JWTData>



}
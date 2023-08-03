package com.example.stajtvplus.models

data class Category(
    val limit: Int,
    val products: List<Product>,
    val skip: Int,
    val total: Int
)
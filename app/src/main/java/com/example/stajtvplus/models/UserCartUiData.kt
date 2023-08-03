package com.example.stajtvplus.models


data class UserCartUiData(
    val id: Long,
    val price: Long,
    val quantity: Long,
    val title: String,
    val imageUrl: List<String>,
)


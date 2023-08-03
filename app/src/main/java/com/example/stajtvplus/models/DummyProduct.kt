package com.example.stajtvplus.models


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class DummyProduct (
    val products: List<Product>,
    val total: Long,
    val skip: Long,
    val limit: Long
)
@Parcelize
data class Product (
    val id: Long,
    val title: String,
    val description: String,
    val price: Long,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Long,
    val brand: String,
    val category: String,
    val thumbnail: String,
    val images: List<String>
) : Parcelable


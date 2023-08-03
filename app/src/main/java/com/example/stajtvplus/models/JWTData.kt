package com.example.stajtvplus.models

data class JWTData (
    val id: Long,
    val username: String?,
    val email: String,
    val firstName: String?,
    val lastName: String?,
    val age: String?,
    val phone: String,
    val birthDate:String?,
    val image: String?,
    val token: String?,
    val address: Address
)
data class Address(
    val address: String,
    val city: String
)
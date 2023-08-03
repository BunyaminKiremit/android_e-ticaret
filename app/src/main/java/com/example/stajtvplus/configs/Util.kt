package com.example.stajtvplus

import com.example.stajtvplus.models.JWTData
import com.example.stajtvplus.models.Product


class Util {

    companion object {
        var user : JWTData? = null
        var products = listOf<Product>()
        var fcmToken: String? = null

    }

}
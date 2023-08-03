package com.example.stajtvplus.ui.home

import androidx.lifecycle.ViewModel
import com.example.stajtvplus.models.Product

class SharedViewModel : ViewModel() {
    var selectedProduct: Product? = null
}
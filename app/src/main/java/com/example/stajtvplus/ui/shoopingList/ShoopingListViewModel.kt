package com.example.stajtvplus.ui.shoopingList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.stajtvplus.models.UserCartUiData

class ShoopingListViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _cartList: MutableLiveData<List<UserCartUiData>> = savedStateHandle.getLiveData(KEY_CART_LIST, emptyList())
    val cartList: LiveData<List<UserCartUiData>> = _cartList

    fun addItemToCart(item: UserCartUiData) {
        val currentList = _cartList.value.orEmpty().toMutableList()
        currentList.add(item)
        _cartList.value = currentList
    }

    fun removeItemFromCart(item: UserCartUiData) {
        val currentList = _cartList.value.orEmpty().toMutableList()
        currentList.remove(item)
        _cartList.value = currentList
    }

    companion object {
        private const val KEY_CART_LIST = "key_cart_list"
    }
}


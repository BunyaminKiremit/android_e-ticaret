package com.example.stajtvplus.ui.shoopingList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stajtvplus.MainActivity
import com.example.stajtvplus.databinding.ShoppingListItemBinding
import com.example.stajtvplus.models.UserCartUiData


class ShoppingListAdapter(private val onItemLongClicked: (UserCartUiData) -> Unit) : ListAdapter<UserCartUiData, ShoppingListAdapter.ShoppingListViewHolder>(ShoppingListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val binding = ShoppingListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShoppingListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ShoppingListViewHolder(private val binding: ShoppingListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(cart: UserCartUiData) {
            binding.apply {
                Glide.with(itemView).load(cart.imageUrl[0]).centerCrop().into(imageView)
                itemNameTextView.text = cart.title
                itemPriceTextView.text = "${cart.price} $"
                itemQuantity.text = cart.quantity.toString()
            }

            binding.incrementButton.setOnClickListener {
                val updatedCart = cart.copy(quantity = cart.quantity + 1)
                submitUpdatedCart(updatedCart)
            }

            binding.decrementButton.setOnClickListener {
                if (cart.quantity > 1) {
                    val updatedCart = cart.copy(quantity = cart.quantity - 1)
                    submitUpdatedCart(updatedCart)
                } else if (cart.quantity <= 1) {
                    removeItem(cart) // Eğer ürün miktarı 1 ise, ürünü sepetten çıkar
                }
            }

            binding.root.setOnLongClickListener {
                onItemLongClicked(cart)
                true
            }
        }

        private fun submitUpdatedCart(updatedCart: UserCartUiData) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val newList = currentList.toMutableList()
                newList[position] = updatedCart
                submitList(newList)
            }
        }

        private fun removeItem(cart: UserCartUiData) {
            val position = currentList.indexOf(cart)
            if (position != RecyclerView.NO_POSITION) {
                val newList = currentList.toMutableList()
                newList.removeAt(position)
                submitList(newList)
            }
        }
    }

    private class ShoppingListDiffCallback : DiffUtil.ItemCallback<UserCartUiData>() {
        override fun areItemsTheSame(oldItem: UserCartUiData, newItem: UserCartUiData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserCartUiData, newItem: UserCartUiData): Boolean {
            return oldItem == newItem
        }
    }
}
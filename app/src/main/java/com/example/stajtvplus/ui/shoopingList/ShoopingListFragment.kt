package com.example.stajtvplus.ui.shoopingList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.stajtvplus.MainActivity
import com.example.stajtvplus.R
import com.example.stajtvplus.databinding.FragmentShoopinglistBinding
import com.example.stajtvplus.models.UserCartUiData

class ShoopingListFragment : Fragment() {

    private var _binding: FragmentShoopinglistBinding? = null
    private lateinit var shoppingListAdapter: ShoppingListAdapter
    private val shoppingListViewModel: ShoopingListViewModel by viewModels {
        SavedStateViewModelFactory(requireActivity().application, this)
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoopinglistBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView: RecyclerView = root.findViewById(R.id.cart_listview)

        shoppingListAdapter = ShoppingListAdapter { selectedCart ->
            shoppingListViewModel.removeItemFromCart(selectedCart)
        }
        recyclerView.adapter = shoppingListAdapter

        // Observe changes to the cartList
        shoppingListViewModel.cartList.observe(viewLifecycleOwner) { cartList ->
            shoppingListAdapter.submitList(cartList)
        }

        MainActivity.selected.observe(viewLifecycleOwner) { selectedProduct ->
            selectedProduct?.let {
                val newCartItem = UserCartUiData(it.id, it.price, 1, it.title, it.images)
                shoppingListViewModel.addItemToCart(newCartItem)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.stajtvplus.ui.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.stajtvplus.MainActivity
import com.example.stajtvplus.R
import com.example.stajtvplus.databinding.FragmentProductDetailBinding
import com.example.stajtvplus.models.Product
import com.example.stajtvplus.ui.home.HomeFragment
import com.example.stajtvplus.ui.home.SharedViewModel

class ProductDetailFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by viewModels()

    companion object {
        private const val ARG_SELECTED_PRODUCT = "selected_product"

        fun newInstance(selectedProduct: Product): ProductDetailFragment {
            val args = Bundle()
            args.putParcelable(ARG_SELECTED_PRODUCT, selectedProduct)
            val fragment = ProductDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var binding: FragmentProductDetailBinding

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)


        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Geri düğmesine basıldığında bu metot çalışacak.
                // Fragmentı geri yığından çıkaralım.
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        val product = MainActivity.selectedProduct
        product?.let {
            Glide.with(requireContext()).load(it.images[0]).into(binding.imgProductImage)
            binding.txtTitle.text = it.title
            binding.txtRate.text = "Rating : ${it.rating}"
            binding.txtDescription.text = it.description
            binding.txtCategory.text = "Category : ${it.category}"
            binding.txtBrand.text = "Brand : ${it.brand}"
            binding.txtStock.text = "Stock: ${it.stock}"
            binding.txtDPercentage.text = "with ${it.discountPercentage}% discount"
            binding.txtPrice.text = "Price: ${it.price} $ "
        }

        binding.button.setOnClickListener {
            MainActivity.selected.value=product
            val navController = findNavController()
            navController.navigate(R.id.navigation_home)
        }
    }
}
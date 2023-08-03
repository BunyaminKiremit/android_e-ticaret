package com.example.stajtvplus.ui.home


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stajtvplus.MainActivity
import com.example.stajtvplus.R
import com.example.stajtvplus.Util
import com.example.stajtvplus.configs.ApiClient
import com.example.stajtvplus.databinding.FragmentHomeBinding
import com.example.stajtvplus.models.Category
import com.example.stajtvplus.models.DummyProduct
import com.example.stajtvplus.models.Product
import com.example.stajtvplus.services.IDummyService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var dummyService: IDummyService
    private lateinit var customProductsAdapter: CustomProductsAdapter




    private val _categories = MutableLiveData<List<String>>()
    val categoriesList: LiveData<List<String>> get() = _categories

    private val binding get() = _binding!!




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dummyService = ApiClient.getClient().create(IDummyService::class.java)

        customProductsAdapter = CustomProductsAdapter(requireActivity())
        binding.productsListView.adapter = customProductsAdapter

        categoriesList.observe(viewLifecycleOwner, { categories ->
            displayCategories(categories)
        })


        binding.productsListView.setOnItemClickListener { parent, view, position, id ->
            if (position >= 0 && position < Util.products.size) {
                MainActivity.selectedProduct= Util.products[position]
                val navController = findNavController()
                navController.navigate(R.id.navigation_productdetail)
            } else {
                showToast("Geçersiz ürün konumu: $position")
            }
        }


        getProducts()
        getCategories()
        binding.etSearchProduct.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val filterText = s.toString().trim()
                filterProducts(filterText)
            }
        })

        return root
    }

    private fun displayCategories(categories: List<String>) {
        val categoryAdapter = CategoryAdapter(requireContext(), categories, object : CategoryAdapter.OnItemClickListener {
            override fun onItemClick(category: String) {
                // Seçilen kategoriye göre ürünleri getir
                getProductsByCategory(category)
            }
        })
        binding.categoryList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.categoryList.adapter = categoryAdapter
    }

    fun getProductsByCategory(category: String) {
        dummyService.getProductsByCategory(category).enqueue(object : Callback<Category> {
            override fun onResponse(call: Call<Category>, response: Response<Category>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    // Kategoriye ait ürünleri customProductsAdapter'a göndererek listele
                    customProductsAdapter.submitList(responseBody.products)
                } else {
                    showToast("Ürünler alınırken hata oluştu.")
                }
            }

            override fun onFailure(call: Call<Category>, t: Throwable) {
                showToast("Ürünler alınırken hata oluştu.")
            }
        })
    }



    private fun filterProducts(filterText: String) {
        dummyService.filterProducts(filterText).enqueue(object : Callback<DummyProduct>{
            override fun onResponse(call: Call<DummyProduct>, response: Response<DummyProduct>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    Util.products = responseBody.products
                    customProductsAdapter.submitList(Util.products)
                } else {
                    showToast("Ürünler alınırken hata oluştu.")
                }
            }

            override fun onFailure(call: Call<DummyProduct>, t: Throwable) {
                showToast("Ürünler filtrelenirken hata oluştu.")
            }
        })
    }


    private fun getProducts() {
        dummyService.products().enqueue(object : Callback<DummyProduct> {
            override fun onResponse(call: Call<DummyProduct>, response: Response<DummyProduct>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    Util.products = responseBody.products // Assuming responseBody.products is a list of products
                    customProductsAdapter.submitList(Util.products)
                } else {
                    showToast("Ürünler alınırken hata oluştu.")
                }
            }

            override fun onFailure(call: Call<DummyProduct>, t: Throwable) {
                showToast("Ürünler alınırken hata oluştu.")
            }
        })
    }

    fun getCategories(){

        val client = dummyService.getCategories()

        client.enqueue(object: Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                val responseBody = response.body()
                if(!response.isSuccessful || responseBody == null){
                    return
                }
                _categories.postValue(responseBody as List<String>)
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


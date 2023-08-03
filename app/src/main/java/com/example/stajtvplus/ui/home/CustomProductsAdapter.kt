package com.example.stajtvplus.ui.home

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stajtvplus.R
import com.example.stajtvplus.models.Product

class CustomProductsAdapter(private val context : Activity)
    : ArrayAdapter<Product>(context,R.layout.product_item){

    private var productList = listOf<Product>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rootView = LayoutInflater.from(context).inflate(R.layout.product_item,null,true)

        val productImageView = rootView.findViewById<ImageView>(R.id.imgProductImageView)
        val productTitle = rootView.findViewById<TextView>(R.id.txtProductTitle)
        val productDescription=rootView.findViewById<TextView>(R.id.txtDescription)
        val productPrice = rootView.findViewById<TextView>(R.id.txtProductPrice)
        val productRate = rootView.findViewById<TextView>(R.id.txtProductRate)

        val product = productList[position]

        Glide.with(rootView).load(product.thumbnail).into(productImageView)
        productTitle.text = product.title
        productDescription.text=product.description
        productPrice.text = product.price.toString() + " $"
        productRate.text = product.rating.toString()


        return rootView
    }

    override fun getCount(): Int {
        return productList.count()
    }

    fun submitList(list : List<Product>){
        productList = list
        notifyDataSetChanged()
    }
}
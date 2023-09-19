package com.hrushabhb.beespokeai

import CartViewModel
import Product
import ProductAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class ProductFragment : Fragment() {

    private lateinit var productRecyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private val productList = mutableListOf<Product>()
    private lateinit var cartViewModel: CartViewModel

    override fun onCreateView(inflater: LayoutInflater, container:ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_product, container, false)

        productRecyclerView = view.findViewById(R.id.productRecyclerView)
        productAdapter = ProductAdapter(productList)
        productRecyclerView.layoutManager = LinearLayoutManager(activity)
        productRecyclerView.adapter = productAdapter

        cartViewModel = ViewModelProvider(requireActivity()).get(CartViewModel::class.java)
        fetchProducts()

        productAdapter.setOnItemClickListener(object : ProductAdapter.OnItemClickListener {
            override fun onItemClick(product: Product) {
                addToCart(product)
            }
        })

        return view
    }

    private fun addToCart(product: Product) {
        cartViewModel.addToCart(product)
    }

    private fun fetchProducts() {
        GlobalScope.launch {
            val apiUrl = "https://fakestoreapi.com/products?limit=30"

            try {
                val url = URL(apiUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    reader.close()

                    val jsonArray = JSONArray(response.toString())
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                        val product = Product(
                            id = jsonObject.getInt("id"),
                            title = jsonObject.getString("title"),
                            price = jsonObject.getString("price"),
                            description = jsonObject.getString("description"),
                            imageUrl = jsonObject.getString("image"),
                            category = jsonObject.getString("category")
                        )
                        productList.add(product)
                    }

                    withContext(Dispatchers.Main) {
                        productAdapter.notifyDataSetChanged()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
package com.hrushabhb.beespokeai

import CartAdapter
import CartViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView

class CartFragment : Fragment() {
    private lateinit var cartViewModel: CartViewModel
    private lateinit var recyclerViewCartItems: RecyclerView
    private lateinit var cartAdapter: CartAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        recyclerViewCartItems = view.findViewById(R.id.recyclerViewCartItems)
        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)

        cartAdapter = CartAdapter()
        recyclerViewCartItems.adapter = cartAdapter

        cartViewModel.cartItems.observe(viewLifecycleOwner, Observer { cartItems ->
            cartAdapter.submitList(cartItems)
        })

        return view
    }
}
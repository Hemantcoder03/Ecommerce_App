package com.hemant.e_commerceapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hemant.e_commerceapp.adapters.CartAdapter
import com.hemant.e_commerceapp.databinding.FragmentCartBinding
import com.hemant.e_commerceapp.models.CategoryData
import com.hemant.e_commerceapp.models.ProductData

class CartFragment : Fragment() {

    private lateinit var binding : FragmentCartBinding
    private lateinit var adapter : CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCartBinding.inflate(inflater, container, false)

        var list : MutableList<ProductData> = mutableListOf()
        list.add(ProductData(1,"Shirt",100,"Desc", emptyList(), CategoryData(1,"","")))
        list.add(ProductData(1,"Pants",500,"Desc", emptyList(), CategoryData(1,"","")))

        adapter = CartAdapter(list,requireContext())
        binding.cartRV.setLayoutManager(LinearLayoutManager(requireActivity()))
        binding.cartRV.setAdapter(adapter)

        // Inflate the layout for this fragment
        return binding.root
    }
}
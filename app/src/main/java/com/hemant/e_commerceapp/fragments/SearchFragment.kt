package com.hemant.e_commerceapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hemant.e_commerceapp.adapters.ProductsAdapter
import com.hemant.e_commerceapp.api_service.ApiInterface
import com.hemant.e_commerceapp.databinding.FragmentSearchBinding
import com.hemant.e_commerceapp.models.ProductData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var productsAdapter: ProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSearchBinding.inflate(layoutInflater)

        productsAdapter = ProductsAdapter(emptyList<ProductData>().toMutableList(),requireContext())

        getALlProducts()

        binding.progressBar.visibility = View.VISIBLE

        binding.searchBar.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                getSearchedProducts(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun getALlProducts() {

        //fetch the products using base url
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.escuelajs.co/api/v1/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val apiService : ApiInterface = retrofit.create(ApiInterface::class.java)
        val call = apiService.getProducts()
        call.enqueue(object : Callback<ArrayList<ProductData>> {
            override fun onResponse(call: Call<ArrayList<ProductData>>, response: Response<ArrayList<ProductData>>) {
                if(response.isSuccessful){
                    //get the successful response and set on gridview
                    val list : MutableList<ProductData> = response.body()!!

                    //if list is empty means no product set no found text
                    if(list.isEmpty()){
                        binding.noProductFound.visibility = View.VISIBLE
                    }
                    else{
                        binding.noProductFound.visibility = View.GONE
                    }

                    //hide the progress bar and show the contains
                    binding.progressBar.visibility = View.GONE

                    try{
                        productsAdapter = ProductsAdapter(list, requireContext())
                        binding.searchProductGrid.adapter = productsAdapter
                    }catch (ignored : Exception){
                    }
                }
                else{
                    //hide the progress bar and show the error
                    binding.progressBar.visibility = View.GONE
                    binding.noProductFound.visibility = View.VISIBLE
                    Toast.makeText(requireContext(),response.errorBody()!!.charStream().read().toString(),
                        Toast.LENGTH_SHORT).show()
                    Log.d("checkError","e "+response.errorBody()!!.charStream().read().toString())
                }
            }

            override fun onFailure(call: Call<ArrayList<ProductData>>, t: Throwable) {
                //hide the progress bar and show the error
                binding.progressBar.visibility = View.GONE
                binding.noProductFound.visibility = View.VISIBLE

                Toast.makeText(requireContext(),t.message, Toast.LENGTH_SHORT).show()
                Log.d("checkError",t.message.toString())
            }
        })
    }

    private fun getSearchedProducts(s : String) {

        //fetch the products using base url
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.escuelajs.co/api/v1/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val apiService : ApiInterface = retrofit.create(ApiInterface::class.java)
        val call = apiService.getSearchedProducts(s)
        call.enqueue(object : Callback<ArrayList<ProductData>> {
            override fun onResponse(call: Call<ArrayList<ProductData>>, response: Response<ArrayList<ProductData>>) {
                if(response.isSuccessful){
                    //get the successful response and set on gridview
                    val list : MutableList<ProductData> = response.body()!!

                    //if list is empty means no product set no found text
                    if(list.isEmpty()){
                        binding.noProductFound.visibility = View.VISIBLE
                    }
                    else{
                        binding.noProductFound.visibility = View.GONE
                    }

                    try{
                        productsAdapter = ProductsAdapter(list, requireContext())
                        binding.searchProductGrid.adapter = productsAdapter
                    }catch (ignored : Exception){
                    }
                }
                else{
                    Toast.makeText(requireContext(),response.errorBody()!!.charStream().read().toString(),
                        Toast.LENGTH_SHORT).show()
                    Log.d("checkError","e "+response.errorBody()!!.charStream().read().toString())
                }
            }

            override fun onFailure(call: Call<ArrayList<ProductData>>, t: Throwable) {
                Toast.makeText(requireContext(),t.message, Toast.LENGTH_SHORT).show()
                Log.d("checkError",t.message.toString())
            }
        })
    }
}
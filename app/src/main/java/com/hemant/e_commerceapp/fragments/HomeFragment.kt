package com.hemant.e_commerceapp.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.hemant.e_commerceapp.R
import com.hemant.e_commerceapp.adapters.ProductsAdapter
import com.hemant.e_commerceapp.api_service.ApiInterface
import com.hemant.e_commerceapp.databinding.FragmentHomeBinding
import com.hemant.e_commerceapp.models.ProductData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor : SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        //initiate the sharedPreference (local database)
        sharedPreferences = requireContext().getSharedPreferences("My_Pref",0)
        editor = sharedPreferences.edit()

        binding.progressBar.visibility = View.VISIBLE

        //set the click listener on filter button
        binding.sortBtn.setOnClickListener {
            setSort()
        }

        binding.filterBtn.setOnClickListener {
            setFilter()
        }

        //fetch all the products from api
        getALlProducts()
        
        // Inflate the layout for requireContext() fragment
        return binding.root
    }

    private fun setFilter(){

        val builder = BottomSheetDialog(requireContext())
        builder.setContentView(R.layout.filter_items)
        builder.setCancelable(true)

        val filterOneToHund = builder.findViewById<CheckBox>(R.id.filterOneToHund)
        val filterHundToThousand = builder.findViewById<CheckBox>(R.id.filterHundToThousand)
        val filterOneThousToFiveThous = builder.findViewById<CheckBox>(R.id.filterOneThousToFiveThous)
        val chip1 = builder.findViewById<Chip>(R.id.chip1)
        val chip2 = builder.findViewById<Chip>(R.id.chip2)
        val chip3 = builder.findViewById<Chip>(R.id.chip3)
        val chip4 = builder.findViewById<Chip>(R.id.chip4)
        val chip5 = builder.findViewById<Chip>(R.id.chip5)

        //set the filter on price range
        filterOneToHund!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                getPriceRangeProducts(1,100)
                editor.putInt("priceRange",1)
                editor.commit()
            }
        }

        filterHundToThousand!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                getPriceRangeProducts(100,1000)
                editor.putInt("priceRange",2)
                editor.commit()
            }
        }

        filterOneThousToFiveThous!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                getPriceRangeProducts(1000,5000)
                editor.putInt("priceRange",3)
                editor.commit()
            }
        }

        //set the past selected price range
        if(sharedPreferences.getInt("priceRange",0)==1){
            filterOneToHund.isChecked = true
        }
        else if(sharedPreferences.getInt("priceRange",0)==2){
            filterHundToThousand.isChecked = true
        }
        else if(sharedPreferences.getInt("priceRange",0)==3){
            filterOneThousToFiveThous.isChecked = true
        }

        //set past selected category using sharedpreferences
        if(sharedPreferences.getInt("category",0) == 1){
            setChipSelected(chip1!!)
        }
        else if(sharedPreferences.getInt("category",0) == 2){
            setChipSelected(chip2!!)
        }
        else if(sharedPreferences.getInt("category",0) == 3){
            setChipSelected(chip3!!)
        }
        else if(sharedPreferences.getInt("category",0) == 4){
            setChipSelected(chip4!!)
        }
        else if(sharedPreferences.getInt("category",0) == 5){
            setChipSelected(chip5!!)
        }

        //set the combination filter
        if(sharedPreferences.getInt("category",0) != 0 && sharedPreferences.getInt("priceRange",0) != 0){
            if(sharedPreferences.getInt("priceRange",0) == 1){
                getCombFilterProducts(1,100,sharedPreferences.getInt("category",0))
            }
            else if(sharedPreferences.getInt("priceRange",0) == 2){
                getCombFilterProducts(100,1000,sharedPreferences.getInt("category",0))
            }
            else if(sharedPreferences.getInt("priceRange",0) == 3){
                getCombFilterProducts(1000,5000,sharedPreferences.getInt("category",0))
            }
        }


        chip1!!.setOnClickListener {
            if (!chip1.isChecked) {

                //set the selected to one chip and remove other
                setChipSelected(chip1)
                removeChipSelected(chip2!!)
                removeChipSelected(chip3!!)
                removeChipSelected(chip4!!)
                removeChipSelected(chip5!!)

                //set category and send id for distribution
                getCategoryProducts(1)
                editor.putInt("category",1)
                editor.commit()
            }
        }

        chip2!!.setOnClickListener {
            if(!chip2!!.isChecked){

                //set the selected to one chip and remove other
                setChipSelected(chip2)
                removeChipSelected(chip1)
                removeChipSelected(chip3!!)
                removeChipSelected(chip4!!)
                removeChipSelected(chip5!!)

                //set category and send id for distribution
                getCategoryProducts(2)
                editor.putInt("category",2)
                editor.commit()
            }
        }

        chip3!!.setOnClickListener {
            if(!chip3!!.isChecked){

                //set the selected to one chip and remove other
                setChipSelected(chip3)
                removeChipSelected(chip2)
                removeChipSelected(chip1)
                removeChipSelected(chip4!!)
                removeChipSelected(chip5!!)

                //set category and send id for distribution
                getCategoryProducts(3)
                editor.putInt("category",3)
                editor.commit()
            }
        }

        chip4!!.setOnClickListener {
            if(!chip4!!.isChecked){

                //set the selected to one chip and remove other
                setChipSelected(chip4)
                removeChipSelected(chip2)
                removeChipSelected(chip3)
                removeChipSelected(chip1)
                removeChipSelected(chip5!!)

                //set category and send id for distribution
                getCategoryProducts(4)
                editor.putInt("category",4)
                editor.commit()
            }
        }

        chip5!!.setOnClickListener {
            if(!chip5!!.isChecked){

                //set the selected to one chip and remove other
                setChipSelected(chip5)
                removeChipSelected(chip2)
                removeChipSelected(chip3)
                removeChipSelected(chip4)
                removeChipSelected(chip1)

                //set category and send id for distribution
                getCategoryProducts(5)
                editor.putInt("category",5)
                editor.commit()
            }
        }
        builder.show()
    }

    private fun setSort(){

        val builder = BottomSheetDialog(requireContext())
        builder.setContentView(R.layout.sort_items)
        builder.setCancelable(true)

        val sortGroup = builder.findViewById<RadioGroup>(R.id.sortGroup)
        val sortLowToHigh = builder.findViewById<RadioButton>(R.id.sortLowToHigh)
        val sortHighToLow = builder.findViewById<RadioButton>(R.id.sortHighToLow)

        if(sharedPreferences.getString("sort","").equals("Ascending")){
            sortLowToHigh!!.isChecked = true
        }
        else{
            sortHighToLow!!.isChecked = true
        }

        sortGroup!!.setOnCheckedChangeListener { group, checkedId ->
            if(sortLowToHigh!!.isChecked){
                //store sort and apply
                getSorts("Ascending")
                editor.putString("sort","Ascending")
            }
            else{
                editor.putString("sort","Descending")
                getSorts("Descending")
            }
            editor.commit()

        }
        builder.show()
    }

    private fun getALlProducts(){

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

                    //hide the progress bar and show the error
                    binding.progressBar.visibility = View.GONE

                    productsAdapter = ProductsAdapter(list, requireContext())
                    binding.productsGrid.adapter = productsAdapter
                }
                else{
                    //hide the progress bar and show the error
                    binding.progressBar.visibility = View.GONE
                    binding.noProductFound.visibility = View.VISIBLE
                    Toast.makeText(requireContext(),response.errorBody()!!.charStream().read().toString(),Toast.LENGTH_SHORT).show()
                    Log.d("checkError","e "+response.errorBody()!!.charStream().read().toString())
                }
            }

            override fun onFailure(call: Call<ArrayList<ProductData>>, t: Throwable) {
                //hide the progress bar and show the error
                binding.progressBar.visibility = View.GONE
                binding.noProductFound.visibility = View.VISIBLE
                Toast.makeText(requireContext(),t.message,Toast.LENGTH_SHORT).show()
                Log.d("checkError",t.message.toString())
            }
        })
    }

    private fun getCategoryProducts(categoryId : Int) {

        //fetch the products using base url
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.escuelajs.co/api/v1/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val apiService : ApiInterface = retrofit.create(ApiInterface::class.java)
        val call = apiService.getCategoryProducts(categoryId = categoryId)
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

                    //hide the progress bar and show the error
                    binding.progressBar.visibility = View.GONE

                    productsAdapter = ProductsAdapter(list, requireContext())
                    binding.productsGrid.adapter = productsAdapter
                }
                else{
                    //hide the progress bar and show the error
                    binding.progressBar.visibility = View.GONE
                    binding.noProductFound.visibility = View.VISIBLE
                    Toast.makeText(requireContext(),response.errorBody()!!.charStream().read().toString(),Toast.LENGTH_SHORT).show()
                    Log.d("checkError","e "+response.errorBody()!!.charStream().read().toString())
                }
            }

            override fun onFailure(call: Call<ArrayList<ProductData>>, t: Throwable) {
                //hide the progress bar and show the error
                binding.progressBar.visibility = View.GONE
                binding.noProductFound.visibility = View.VISIBLE
                Toast.makeText(requireContext(),t.message,Toast.LENGTH_SHORT).show()
                Log.d("checkError",t.message.toString())
            }
        })
    }

    private fun getCombFilterProducts(price_min: Int, price_max: Int,categoryId : Int) {

        //fetch the products using base url
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.escuelajs.co/api/v1/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val apiService : ApiInterface = retrofit.create(ApiInterface::class.java)
        val call = apiService.getCombFilterProducts(price_min,price_max,categoryId)
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

                    //hide the progress bar and show the error
                    binding.progressBar.visibility = View.GONE

                    productsAdapter = ProductsAdapter(list, requireContext())
                    binding.productsGrid.adapter = productsAdapter
                }
                else{
                    //hide the progress bar and show the error
                    binding.progressBar.visibility = View.GONE
                    binding.noProductFound.visibility = View.VISIBLE
                    Toast.makeText(requireContext(),response.errorBody()!!.charStream().read().toString(),Toast.LENGTH_SHORT).show()
                    Log.d("checkError","e "+response.errorBody()!!.charStream().read().toString())
                }
            }

            override fun onFailure(call: Call<ArrayList<ProductData>>, t: Throwable) {
                //hide the progress bar and show the error
                binding.progressBar.visibility = View.GONE
                binding.noProductFound.visibility = View.VISIBLE
                Toast.makeText(requireContext(),t.message,Toast.LENGTH_SHORT).show()
                Log.d("checkError",t.message.toString())
            }
        })
    }


    private fun getPriceRangeProducts(price_min :Int, price_max : Int) {

        //fetch the products using base url
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.escuelajs.co/api/v1/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val apiService : ApiInterface = retrofit.create(ApiInterface::class.java)
        val call = apiService.getPriceRangeProducts(price_min,price_max)
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

                    //hide the progress bar and show the error
                    binding.progressBar.visibility = View.GONE

                    productsAdapter = ProductsAdapter(list, requireContext())
                    binding.productsGrid.adapter = productsAdapter
                }
                else{
                    //hide the progress bar and show the error
                    binding.progressBar.visibility = View.GONE
                    binding.noProductFound.visibility = View.VISIBLE
                    Toast.makeText(requireContext(),response.errorBody()!!.charStream().read().toString(),Toast.LENGTH_SHORT).show()
                    Log.d("checkError","e "+response.errorBody()!!.charStream().read().toString())
                }
            }

            override fun onFailure(call: Call<ArrayList<ProductData>>, t: Throwable) {
                //hide the progress bar and show the error
                binding.progressBar.visibility = View.GONE
                binding.noProductFound.visibility = View.VISIBLE
                Toast.makeText(requireContext(),t.message,Toast.LENGTH_SHORT).show()
                Log.d("checkError",t.message.toString())
            }
        })
    }

    private fun getSorts(s: String) {

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

                    //hide the progress bar and show the error
                    binding.progressBar.visibility = View.GONE

                    //sort
                    if(s.equals("Ascending")){
                        val sortedByPrice = response.body()!!.sortedBy { it.price.toString() }
                        productsAdapter = ProductsAdapter(sortedByPrice.toMutableList(), requireContext())
                        binding.productsGrid.adapter = productsAdapter
                    }
                    else{
                        val sortedByPrice = response.body()!!.sortedByDescending { it.price.toString() }
                        productsAdapter = ProductsAdapter(sortedByPrice.toMutableList(), requireContext())
                        binding.productsGrid.adapter = productsAdapter
                    }
                }
                else{
                    //hide the progress bar and show the error
                    binding.progressBar.visibility = View.GONE
                    binding.noProductFound.visibility = View.VISIBLE
                    Toast.makeText(requireContext(),response.errorBody()!!.charStream().read().toString(),Toast.LENGTH_SHORT).show()
                    Log.d("checkError","e "+response.errorBody()!!.charStream().read().toString())
                }
            }

            override fun onFailure(call: Call<ArrayList<ProductData>>, t: Throwable) {
                //hide the progress bar and show the error
                binding.progressBar.visibility = View.GONE
                binding.noProductFound.visibility = View.VISIBLE
                Toast.makeText(requireContext(),t.message,Toast.LENGTH_SHORT).show()
                Log.d("checkError",t.message.toString())
            }
        })
    }

    private fun setChipSelected(chip : Chip){
        chip.isChecked = true
        chip.setTextColor(resources.getColor(R.color.white))
        chip.setChipBackgroundColorResource(R.color.app_theme_color)
    }

    private fun removeChipSelected(chip : Chip){
        chip.isChecked = false
        chip.setTextColor(resources.getColor(R.color.black))
        chip.setChipBackgroundColorResource(R.color.light_grey)
    }
    
}
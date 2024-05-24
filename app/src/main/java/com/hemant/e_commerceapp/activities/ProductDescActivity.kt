package com.hemant.e_commerceapp.activities

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hemant.e_commerceapp.R
import com.hemant.e_commerceapp.adapters.ProductsAdapter
import com.hemant.e_commerceapp.api_service.ApiInterface
import com.hemant.e_commerceapp.databinding.ActivityProductDescBinding
import com.hemant.e_commerceapp.databinding.FragmentCartBinding
import com.hemant.e_commerceapp.models.ProductData
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductDescActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDescBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductDescBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id : Int = intent.getIntExtra("id",0)
        getProduct(id)
    }

    private fun getProduct(id : Int){

        //fetch the products using base url
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.escuelajs.co/api/v1/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val apiService : ApiInterface = retrofit.create(ApiInterface::class.java)
        val call = apiService.getProduct(id)
        call.enqueue(object : Callback<ProductData> {
            override fun onResponse(call: Call<ProductData>, response: Response<ProductData>) {
                if(response.isSuccessful){
                    //get the successful response and set on gridview
                    val product : ProductData = response.body()!!

                    binding.productName.text = product.title
                    binding.productDesc.text = product.description
                    Picasso.get().load(Uri.parse(product.images[0])).into(binding.productImage)
                }
                else{
                    Toast.makeText(this@ProductDescActivity,response.errorBody()!!.charStream().read().toString(),
                        Toast.LENGTH_SHORT).show()
                    Log.d("checkError","e "+response.errorBody()!!.charStream().read().toString())
                }
            }

            override fun onFailure(call: Call<ProductData>, t: Throwable) {
                Toast.makeText(this@ProductDescActivity,t.message, Toast.LENGTH_SHORT).show()
                Log.d("checkError",t.message.toString())
            }
        })
    }

}
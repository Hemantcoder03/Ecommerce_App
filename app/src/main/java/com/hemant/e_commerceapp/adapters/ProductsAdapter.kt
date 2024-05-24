package com.hemant.e_commerceapp.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.hemant.e_commerceapp.R
import com.hemant.e_commerceapp.activities.ProductDescActivity
import com.hemant.e_commerceapp.models.ProductData
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import java.util.Timer
import java.util.TimerTask

class ProductsAdapter(private var productsList:MutableList<ProductData>, private var context: Context) : BaseAdapter() {

    private var inflater : LayoutInflater?   = null
    private var filterList: MutableList<ProductData> = productsList
    private lateinit var productLL : LinearLayout
    private lateinit var productImage : ImageView
    private lateinit var productNotFound : ImageView
    private lateinit var productName : TextView
    private lateinit var productPrice : TextView
    val handler: Handler = Handler(Looper.getMainLooper())

    override fun getCount(): Int {
        //define the size
        return filterList.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView

        if (inflater == null) {
            //set the inflation if null
            inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        if (convertView == null) {
            //set the layout if null
            convertView = inflater!!.inflate(R.layout.product_item, null)
        }

        productName = convertView!!.findViewById(R.id.productName)
        productImage = convertView.findViewById(R.id.productImage)
//        productNotFound = convertView.findViewById(R.id.productNotFound)
        productPrice = convertView.findViewById(R.id.productPrice)
        productLL = convertView.findViewById(R.id.productLL)

        //set the required actions and text
        val product: ProductData = filterList[position]
        productName.setText(product.title)
        productPrice.setText("$" + product.price.toString())

        Picasso.get().load(product.images[0]).into(productImage)

        //set on click listener when click on item
        productLL.setOnClickListener {

            val intent = Intent(context,ProductDescActivity::class.java)
            intent.putExtra("id",product.id)
            context.startActivity(intent)
        }

        return convertView
    }

}
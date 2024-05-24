package com.hemant.e_commerceapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hemant.e_commerceapp.R
import com.hemant.e_commerceapp.models.ProductData

class CartAdapter(private var cartList: MutableList<ProductData>, private var context: Context) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val cartItemImage : ImageView = itemView.findViewById(R.id.cartItemImage)
        val cartItemName : TextView = itemView.findViewById(R.id.cartItemName)
        val cartItemPrice : TextView = itemView.findViewById(R.id.cartItemPrice)
        val cartItemDecreBtn : ImageButton = itemView.findViewById(R.id.cartItemDecreBtn)
        val cartItemQty : TextView = itemView.findViewById(R.id.cartItemQty)
        val cartItemIncreBtn : ImageButton = itemView.findViewById(R.id.cartItemIncreBtn)
        val cartItemRemoveBtn : ImageButton = itemView.findViewById(R.id.cartItemRemoveBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        val productData = cartList[position]
        holder.cartItemName.text = productData.title
        holder.cartItemPrice.text = productData.price.toString()
        holder.cartItemQty.text = "1"
    }
}
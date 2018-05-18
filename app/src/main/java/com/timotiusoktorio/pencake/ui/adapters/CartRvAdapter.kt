package com.timotiusoktorio.pencake.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.timotiusoktorio.pencake.R
import com.timotiusoktorio.pencake.data.model.CartItem
import com.timotiusoktorio.pencake.data.model.Product
import com.timotiusoktorio.pencake.extensions.inflate
import com.timotiusoktorio.pencake.extensions.loadUrl
import kotlinx.android.synthetic.main.list_item_cart.view.*

class CartRvAdapter(private val cart: MutableList<CartItem> = mutableListOf(),
                    private val listener: (CartItem) -> Unit) : RecyclerView.Adapter<CartRvAdapter.ViewHolder>() {

    fun updateData(data: List<CartItem>) {
        cart.clear()
        cart.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.inflate(R.layout.list_item_cart))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(cart[position])

    override fun getItemCount(): Int = cart.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(cartItem: CartItem) = with(itemView) {
            val product = Product.fromJson(cartItem.productJson)
            productNameTv.text = product.name
            quantityTv.text = context.getString(R.string.string_format_quantity, cartItem.quantity)
            productSizeTv.text = product.prices[cartItem.selectedSizeIndex].size
            specialRequestsTv.text = if (cartItem.specialRequests.isNullOrEmpty()) context.getString(R.string.text_no_special_requests) else cartItem.specialRequests
            subtotalTv.text = context.getString(R.string.string_format_price, cartItem.subtotal)
            productPhotoIv.loadUrl(product.imageUrls[0])
            setOnClickListener { listener(cartItem) }
        }
    }
}
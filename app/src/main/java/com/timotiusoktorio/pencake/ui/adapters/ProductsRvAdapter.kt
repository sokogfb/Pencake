package com.timotiusoktorio.pencake.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.timotiusoktorio.pencake.R
import com.timotiusoktorio.pencake.data.model.Product
import com.timotiusoktorio.pencake.extensions.inflate
import com.timotiusoktorio.pencake.extensions.loadUrl
import kotlinx.android.synthetic.main.list_item_product.view.*

class ProductsRvAdapter(private val products: MutableList<Product> = mutableListOf(),
                        private val listener: (Product) -> Unit) : RecyclerView.Adapter<ProductsRvAdapter.ViewHolder>() {

    fun updateData(data: List<Product>) {
        products.clear()
        products.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.inflate(R.layout.list_item_product))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(products[position])

    override fun getItemCount(): Int = products.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(product: Product) = with(itemView) {
            productNameTv.text = product.name
            productPriceTv.text = context.getString(R.string.string_format_price, product.prices[0].price)
            productPhotoIv.loadUrl(product.imageUrls[0])
            setOnClickListener { listener(product) }
        }
    }
}